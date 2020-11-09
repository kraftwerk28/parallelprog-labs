CFLAGS := `pkg-config --libs --cflags ompi` -lm
BIN := lab6

all:
	@gcc $(CFLAGS) -Wall -o $(BIN) src/main.c

clean:
	@rm -f $(BIN)
