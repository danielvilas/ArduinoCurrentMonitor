function [sizes, outSizes] = calcSize( packet, sizeSamples )
%CALCSIZE Summary of this function goes here
%   Detailed explanation goes here
    if ~ exist('sizeSamples','var')
        sizeSamples=1;
    end
    
    nSamples = 20*sizeSamples;
    max=1024-nSamples;
    
    sizes=ones(max,5);
    outSizes(1:max)=sizeSamples;
    
    for offset=1:1024-nSamples
        data=extractHarmonics(packet,sizeSamples,offset);
        sizes(offset,:)= data;
    end
end

