function [ iaPacket ] = ConvertPacket( packet )
%ConvertPacket Summary of this function goes here
%   Detailed explanation goes here
    %Like Java Data
    [harmonics0, freqs0, average0] =extractHarmonics2(packet,20,1);
    [harmonics4, freqs0, average4] =extractHarmonics2(packet,20,301);
    [harmonics8, freqs0, average8] =extractHarmonics2(packet,20,601);
    [harmonics9, freqs0, average9] =extractHarmonics2(packet,20,1024-400);
    
    out = [-1 -1 -1 -1]; % TV BRay AppleTv IpTv
    isTrain =0;
    isTest = 0;
    
    result = checkTvBdrApple(packet.time);
    if result > 0 
        out = [1 1 1 0]; % TV BRay AppleTv IpTv
        isTrain = result == 1;
        isTest = result == 2;
    end
    
    result = checkTvBdr(packet.time);
    if result > 0 
        out = [1 1 0 0]; % TV BRay AppleTv IpTv
        isTrain = result == 1;
        isTest = result == 2;
    end
    
    result = checkTvIpTv(packet.time);
    if result > 0 
        out = [1 0 0 1]; % TV BRay AppleTv IpTv
        isTrain = result == 1;
        isTest = result == 2;
    end
    
    result = checkOff(packet.time);
    if result > 0 
        out = [0 0 0 0]; % TV BRay AppleTv IpTv
        isTrain = result == 1;
        isTest = result == 2;
    end
    data = [[harmonics0 average0]; [harmonics4 average4]; [harmonics8 average8]; [harmonics9 average9]];
    iaPacket = struct('time', packet.time, 'data', data, 'isTrain', isTrain, 'isTest', isTest, 'out', out);
end

function [result] = checkTvBdrApple(date)
    ref1 = datetime(2017,07,16,15,36,00,'TimeZone','local');
    ref2 = datetime(2017,07,16,16,00,00,'TimeZone','local');
    result = 0;
    if testTrainDate(date,ref1,1,2) || testTrainDate(date,ref2,1,2)
        result = 1;
    elseif testTestDate(date,ref1,1,2) || testTestDate(date,ref2,1,2)
        result = 2;
    end
    
end


function [result] = checkTvBdr(date)
    ref1 = datetime(2017,07,16,17,00,00,'TimeZone','local');
    ref2 = datetime(2017,07,16,20,02,00,'TimeZone','local');
    result = 0;
    if testTrainDate(date,ref1,1,2) || testTrainDate(date,ref2,1,2)
        result = 1;
    elseif testTestDate(date,ref1,1,2) || testTestDate(date,ref2,1,2)
        result = 2;
    end
end

function [result] = checkTvIpTv(date)
    ref1 = datetime(2017,07,16,17,21,00,'TimeZone','local');
    ref2 = datetime(2017,07,16,18,40,00,'TimeZone','local');
    result = 0;
    if testTrainDate(date,ref1,1,2) || testTrainDate(date,ref2,1,2)
        result = 1;
    elseif testTestDate(date,ref1,1,2) || testTestDate(date,ref2,1,2)
        result = 2;
    end 
end

function [result] = checkOff(date)
    ref1 = datetime(2017,07,16,17,40,00,'TimeZone','local');
    ref2 = datetime(2017,07,16,17,45,00,'TimeZone','local');
    ref3 = datetime(2017,07,16,19,00,00,'TimeZone','local');
    ref4 = datetime(2017,07,16,19,05,00,'TimeZone','local');  
 
    result = 0;
    if testTrainDate(date,ref1,1,2) || testTrainDate(date,ref2,1,2) || testTrainDate(date,ref3,1,2) || testTrainDate(date,ref4,1,2)
        result = 1;
    elseif testTestDate(date,ref1,1,2) || testTestDate(date,ref2,1,2) || testTestDate(date,ref3,1,2) || testTestDate(date,ref4,1,2) 
        result = 2;
    end
    
end


function [result] = testTrainDate(date, reference, margin, size)
    start = reference+minutes(margin);
    result = testDate(date, start, size);

end

function [result] = testTestDate(date, reference, margin, size)
    start = reference+minutes(size+margin);
    result = testDate(date, start, size);
end

function [result] = testDate(date, reference,size)
    endTime=reference+minutes(size);
    result = 0;
    if(date>=reference && date <= endTime)
        result=1;
    end
end