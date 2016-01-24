CC=gcc
HEADER_LOC=src/headers
C=src/c
CFLAGS=-I$(HEADER_LOC) -g -lm
FILES=$(C)/*.c $(C)/**/*.c

build: $(FILES)
		$(CC) -o out $(FILES) $(CFLAGS)