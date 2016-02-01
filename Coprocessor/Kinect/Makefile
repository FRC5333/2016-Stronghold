CC=gcc
ROOT=src
HEADER_LOC=$(ROOT)/headers
C_LOC=$(ROOT)/c

CFLAGS=-I$(HEADER_LOC) -I/usr/local/include -g -lm -pthread -L/usr/local/lib/ -lfreenect

FILES=$(C_LOC)/*.c

build: $(FILES)
		$(CC) -o kinect_lib $(FILES) $(CFLAGS)