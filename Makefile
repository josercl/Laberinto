CC=javac
DEST_DIR=bin
FLAGS=-d $(DEST_DIR)

APP=app/Main.java
LOGIC=logic/Casilla.java logic/Grafo.java logic/Nodo.java
GUI=gui/Editor.java gui/Principal.java gui/Util.java

all: $(LOGIC:.java=.class) $(GUI:.java=.class) $(APP:.java=.class) extras

extras:
	cp -R resources bin/;cp -R laberinto bin/

%.class:
	$(CC) $(FLAGS) $*.java

clean:
	$(RM) -r bin/*
