function [ out, lineCount ] = readDataCsv( inputFile )
%READDATACSV Reads the Arduino CSV Rercorded Data
%   Detailed explanation goes here
fid = fopen(inputFile);
tline = fgetl(fid);
i =0;
data=[];
packs=[];
while ischar(tline) % && i<1025
    if startsWith(tline,'#')
        disp(tline);
        if(size(data)>0)
            curr=struct('data',data,'a0Max',max(data(:,3)));
            packs=[packs, curr];
            data=[];
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
        if startsWith(micros,'-')
            %disp(micros);
            m=hex2num(micros{1}(2:end));
            data= [data ; ([time, delta, a0, a1, m])];
        else
            %disp(micros);
            data= [data ; ([time, delta, a0, a1, hex2num(micros)])];
        end
        
    end
    tline = fgetl(fid);
    i=i+1;
end
fclose(fid);

curr=struct('data',data,'a0Max',max(data(:,3)));
packs=[packs, curr];

out=packs;
lineCount = i;
end

