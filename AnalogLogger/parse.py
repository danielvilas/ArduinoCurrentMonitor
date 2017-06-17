import struct
from matplotlib import pyplot as plt
import numpy as np

time=[]
a0=[]
a1=[]

with open("myfile", "rb") as f:
    bytes = f.read(8)
    while bytes:
        #t0=bytes[0]<<24+bytes[1]<<16+bytes[2]<<8+bytes[3]
        data=struct.unpack('>IHH',bytes)
        print(data)
        time.append(data[0])
        a0.append(data[1])
        a1.append(data[2])
        bytes = f.read(8)

#print(a0);

data=np.arange(0,len(time))
plt.plot(data,time)
plt.show()

plt.plot(time,a0,'o')
plt.plot(time,a1)
plt.show();
