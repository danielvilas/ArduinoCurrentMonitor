function [ outPackets ] = ConvertPackets( input_args )
%CONVERTPACKETS Summary of this function goes here
%   Detailed explanation goes here
len = size(input_args);

tmp = struct('time', datetime(), 'data', [], 'isTrain', 0, 'isTest', 0, 'out', 0);
data=repmat(tmp, len,1);
for i= 1:len
    packet = ConvertPacket(input_args(i));
    data(i)=packet;
end

outPackets = data;

end

