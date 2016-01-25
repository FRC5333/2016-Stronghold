CC=gcc
LIB_LOC=src/library
HEADER_LIB_LOC=$(LIB_LOC)/headers
C_LIB_LOC=$(LIB_LOC)/c

IMPL_LOC=src/impl
HEADER_IMPL_LOC=$(IMPL_LOC)/headers
C_IMPL_LOC=$(IMPL_LOC)/c

CFLAGS=-I$(HEADER_LIB_LOC) -I$(IMPL_LOC) -g -lm
FILES=$(C_LIB_LOC)/*.c $(C_LIB_LOC)/**/*.c $(C_IMPL_LOC)/*.c

build: $(FILES)
		$(CC) -o spline_lib $(FILES) $(CFLAGS)