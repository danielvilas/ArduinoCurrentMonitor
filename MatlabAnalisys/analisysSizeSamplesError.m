function [sizes, outSizes] = analisysSizeSamplesError( packet, mode )
%analisysSizeSamples Summary of this function goes here
%   Detailed explanation goes here

if ~ exist('mode','var')
    modeVal=0;
elseif strcmp(mode,'error')
    modeVal=0;
elseif strcmp(mode,'average')
    modeVal=1;
else
    modeVal=0;
end
    
    sizes= [];
    outSizes=[1:50];
    
    for sizeSamples=1:50
        [data, dataOut]=calcSize(packet,sizeSamples);
        if modeVal==0
            sizes=[sizes; max(abs(data - mean(data)))];
        elseif modeVal==1
            sizes=[sizes; mean(data)];
        end
        outSizes(sizeSamples)=sizeSamples;
    end
end

