#include "conversions.h"
#include <stdio.h>
#include <string.h>

void intToBytes(int n, char *bytes) {
    bytes[0] = (n >> 24) & 0xFF;
    bytes[1] = (n >> 16) & 0xFF;
    bytes[2] = (n >> 8) & 0xFF;
    bytes[3] = n & 0xFF;
}

int bytesToInt(char *bytes) {
    int value = 0;
    int i;
    for (i = 0; i < 4; i++) {
        int shift = (4 - 1 - i) * 8;
        value += (bytes[i] & 0x000000FF) << shift;
    }
    return value;
}

float intToFloat(int i) {
    float result;
    memcpy(&result, &i, 4);
    return result;
}

int floatToInt(float i) {
    int result;
    memcpy(&result, &i, 4);
    return result;
}

float bytesToFloat(char *buf) {
    return intToFloat(bytesToInt(buf));
}

void floatToBytes(float f, char *buf) {
    intToBytes(floatToInt(f), buf);
}