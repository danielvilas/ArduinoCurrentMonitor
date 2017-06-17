import serial
import sys

ser = serial.Serial(sys.argv[1], 115200, timeout=0.5)
f = open('myfile', 'wb')

while True:
	bytes = ser.read(size=512);
	if(bytes):
		print (bytes)
		f.write(bytes)
	
