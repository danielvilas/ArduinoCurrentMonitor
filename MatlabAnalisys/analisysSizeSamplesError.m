function [sizes, outSizes] = analisysSizeSamplesError( packet )
%analisysSizeSamples Summary of this function goes here
%   Detailed explanation goes here

    
    sizes= [];
    outSizes=[1:50];
    
    for sizeSamples=1:50
        [data, dataOut]=calcSize(packet,sizeSamples);
        sizes=[sizes; max(abs(data - mean(data)))];
        outSizes(sizeSamples)=sizeSamples;
    end
end

