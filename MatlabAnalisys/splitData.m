%Scripts thats reads the raw data an creates the data sets
if ~ exist('h15','var')
    h15=readDataCsv2('../SomAnalisys/data-20170716-15.csv')
end
%RefTime of Known Data
startTime=datetime([2017,07,16,15,36,00],'TimeZone','local');
startTimeOffset=startTime + minutes(1); %Security Margin
startTimeLimit = startTime + minutes(2); %limit for Train Data

times=[h15.time]; %extrac time data from packets

%Train Data
knownIndex= find(times>startTimeOffset & times <= startTimeLimit);

%Config
sampleSize=15;
samplesSizes=15*20;
samplesLimit = 1024 - samplesSizes;
f=zeros(size(knownIndex,2),4);
for pIndex = 1:size(knownIndex,2)
    packet= h15(knownIndex(pIndex));
    
    offset=1;
    while offset < samplesLimit
        f(pIndex,:)= extractHarmonicsStruct(packet,sampleSize, offset);
        offset=offset+samplesSizes;
    end
    
end