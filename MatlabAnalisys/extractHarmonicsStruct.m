function [ harmonics, freqs ] = extractHarmonicsStruct( packet, sizeSamples, offset )
%EXTRACTHARMONICS Summary of this function goes here
%   Detailed explanation goes here

    if ~ exist('sizeSamples','var')
        sizeSamples=1;
    end
    if ~ exist('offset','var')
        offset=1;
    end

    Fs=1000; %1Khz                
    L = 20*sizeSamples;% Length of signal
    max=offset+L;
    a0=[packet.data(offset:max).a0];

    Y0 = fft(double(a0));
    Y0= fftshift(Y0);
    P0 = abs(Y0/double(L));

    f = (-L/2:L/2-1)*(Fs/L);
    
    i0=10*sizeSamples+1;
    i50=11*sizeSamples+1;
    i150=13*sizeSamples+1;
    i250=15*sizeSamples+1;
    i350=17*sizeSamples+1;
    
    freqs=[f(i0), f(i50), f(i150), f(i250), f(i350)];
    harmonics=[P0(i0), P0(i50), P0(i150), P0(i250), P0(i350)];
end

