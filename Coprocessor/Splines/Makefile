CC=gcc
# CC=arm-frc-linux-gnueabi-gcc
LIB_LOC=src/library
HEADER_LIB_LOC=$(LIB_LOC)/headers
C_LIB_LOC=$(LIB_LOC)/c

IMPL_LOC=src/impl
HEADER_IMPL_LOC=$(IMPL_LOC)/headers
C_IMPL_LOC=$(IMPL_LOC)/c

CFLAGS=-I$(HEADER_LIB_LOC) -I$(HEADER_IMPL_LOC) -g -lm -pthread

ifeq ($(OS),Windows_NT)
	ifeq ($(CC),gcc)       # Apparently OS reads from the system, not the compiler. So if we're not using GCC (something like arm-linux-gnueabi-gcc), it wont include WinSock
		CFLAGS += -lws2_32
	endif
endif

FILES=$(C_LIB_LOC)/*.c $(C_LIB_LOC)/**/*.c $(C_IMPL_LOC)/*.c

build: $(FILES)
		$(CC) -o spline_lib $(FILES) $(CFLAGS)