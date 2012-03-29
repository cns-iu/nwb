#!/bin/bash

CC=g++
CFLAGS= -O5 -Wall
LDFLAGS= -lm -Wall
EXEC=Community convert hierarchy
OBJ1= BinaryGraph.o Community.o
OBJ2= EdgeListGraph.o

all: $(EXEC)

Community : $(OBJ1) CommunityMain.o
	$(CC) -o $@ $^ $(LDFLAGS)

convert : $(OBJ2) ConvertMain.o
	$(CC) -o $@ $^ $(LDFLAGS)

hierarchy : main_hierarchy.o
	$(CC) -o $@ $^ $(LDFLAGS)

##########################################
# Generic rules
##########################################

%.o: %.cpp %.h
	$(CC) -o $@ -c $< $(CFLAGS)

%.o: %.cpp
	$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -f *.o *~ $(EXEC)
