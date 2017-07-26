/*
  Analog Input
 Demonstrates analog input by reading an analog sensor on analog pin 0 and
 turning on and off a light emitting diode(LED)  connected to digital pin 13.
 The amount of time the LED will be on and off depends on
 the value obtained by analogRead().

 The circuit:
 * Potentiometer attached to analog input 0
 * center pin of the potentiometer to the analog pin
 * one side pin (either one) to ground
 * the other side pin to +5V
 * LED anode (long leg) attached to digital output 13
 * LED cathode (short leg) attached to ground

 * Note: because most Arduinos have a built-in LED attached
 to pin 13 on the board, the LED is optional.


 Created by David Cuartielles
 modified 30 Aug 2011
 By Tom Igoe

 This example code is in the public domain.

 http://www.arduino.cc/en/Tutorial/AnalogInput

 */

//Periodo en NS
#define PERIOD 1000
#define OVERFLOW_LIMIT 0xFFFFFFFF - PERIOD
const int sensorPin = A0;    // select the input pin for the potentiometer    // select the pin for the LED
unsigned int sensorValue0 = 0;  // variable to store the value coming from the sensor
unsigned int sensorValue1 = 0; 

unsigned long next;

byte buff[8];//4+2+2
void setup() {
  Serial.begin(115200);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  next = micros()+PERIOD;
  memset(buff,0xFF,sizeof(buff));
  Serial.write(buff,sizeof(buff));
}



unsigned long t0;
unsigned int a0;
unsigned int a1;
int cnt=0;
void loop() {
      unsigned long t0=micros();
      while(t0<next||(next<=PERIOD && t0>=OVERFLOW_LIMIT)){
        t0=micros();
      }
      // read the value from the sensor:
      a0 = analogRead(sensorPin);
      a1 = analogRead(A1);
      next=next+PERIOD;
      
      //Serial.print(sensorValue0);
      //Serial.print(" ");
      //Serial.println(sensorValue1);
      buff[3]=t0&0xFF;
      buff[2]=(t0>>8)&0xFF;
      buff[1]=(t0>>16)&0xFF;
      buff[0]=(t0>>24)&0xFF;
  
      buff[5]=a0&255;
      buff[4]=(a0>>8)&255;
  
      buff[7]=a1&255;
      buff[6]=(a1>>8)&255;
      Serial.write(buff,sizeof(buff));
      cnt=(cnt+1)%1024;
      if(cnt==0){
        memset(buff,0xFF,sizeof(buff));
        Serial.write(buff,sizeof(buff));
      }
      
}
