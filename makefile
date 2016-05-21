NAME = "Game"
FRAME = "65"

all:
	@echo "Compiling..."
	javac src/*.java -d bin/

run: all
	@echo "Running..."
	java -cp ./bin $(NAME) ${FRAME}
clean:
	rm -f bin/*.class
