#ifndef CONVERSIONS_H
#define CONVERSIONS_H

void intToBytes(int n, char *bytes);
int bytesToInt(char *bytes);
float intToFloat(int i);
int floatToInt(float i);
float bytesToFloat(char *buf);
void floatToBytes(float f, char *buf);

#endif