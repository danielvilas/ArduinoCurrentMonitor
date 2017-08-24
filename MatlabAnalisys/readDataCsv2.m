function [ out, lineCount ] = readDataCsv2( inputFile )
%READDATACSV Reads the Arduino CSV Rercorded Data
%   Detailed explanation goes here
fid = fopen(inputFile);
tline = fgetl(fid);
i =0;
tmp=struct('time',datetime(),'delta', 0, 'a0', 0, 'a1', 0,'micros', 0);
data=repmat(tmp, 1024,1);
tmp2=struct('data',tmp,'a0Max',max([tmp.a0]));
packs=repmat(tmp2, 600,1);
j=1;
k=1;
while ischar(tline) % && i<1025
    if strcmp(tline(1),'#')
        disp(tline);
        if(i>0)
            curr=struct('data',data,'a0Max',max([data.a0]));
            packs(k)=curr;
            k=k+1;
            data=repmat(tmp, 1024,1);
            j=1;
        end
    else
        %disp(tline);
        C=textscan(tline,'%s %u16 %u16 %d64 %u16', 'Delimiter', ',');
        micros = C{1};
        a0=int16(C{2}) - 511;
        a1=int16(C{3}) - 511;
        time=C{4};
        delta = C{5};
        %disp(([time, delta, a0, a1, hex2num(micros)]));
            %disp(micros);
        m=micros;
         if strcmp(micros{1}(1),'-')
            %disp(micros);
            m=hex2dec(micros{1}(2:end));
        else
            %disp(micros);
            m=hex2dec(micros);
         end
        ndata=struct('time',datetime(time/1000.0,'ConvertFrom', 'posixtime','TimeZone','local'),'delta', delta, 'a0', a0, 'a1', a1,'micros', m);
        data(j) = ndata;
        j=j+1;
    end
    tline = fgetl(fid);
    i=i+1;
end
fclose(fid);

curr=struct('data',data,'a0Max',max([data.a0]));
packs(k)=curr;

out=packs(1:k);
lineCount = i;
end

