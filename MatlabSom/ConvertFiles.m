tmp = readDataCsv2('SomAnalisys/data-20170716-15.csv')
save('Data/data-20170716-15.csv2.mat','tmp')
fft = ConvertPackets(tmp)
save('Data/data-20170716-15.fft.mat','fft')