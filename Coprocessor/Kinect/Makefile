CC=gcc
# CC=arm-frc-linux-gnueabi-gcc
ROOT=src
HEADER_LOC=$(ROOT)/headers
C_LOC=$(ROOT)/c

CFLAGS=-I$(HEADER_LOC) -I/usr/local/include -g -lm -pthread -L/usr/local/lib/ -lfreenect -framework GLUT -framework OpenGL -framework Cocoa

# ifeq ($(OS),Windows_NT)
# 	ifeq ($(CC),gcc)       # Apparently OS reads from the system, not the compiler. So if we're not using GCC (something like arm-linux-gnueabi-gcc), it wont include WinSock
# 		CFLAGS += -lws2_32
# 	endif
# endif

FILES=$(C_LOC)/*.c

build: $(FILES)
		$(CC) -o kinect_lib $(FILES) $(CFLAGS)