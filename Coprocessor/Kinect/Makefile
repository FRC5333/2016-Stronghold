CC=g++
ROOT=src
HEADER_LOC=$(ROOT)/headers
C_LOC=$(ROOT)/c
CPP_LOC=$(ROOT)/cpp

CFLAGS=-I$(HEADER_LOC) -I/usr/local/include/libfreenect -I/usr/local/include -g -lm -pthread -L/usr/local/lib/ -lfreenect `pkg-config --cflags --libs opencv`

FILES=$(C_LOC)/*.c $(CPP_LOC)/*.cpp

build: $(FILES)
		$(CC) -o kinect_lib $(FILES) $(CFLAGS)
