CPP = g++
INCLUDE = -Iinclude
DEBUG = -g -Wall -Wextra --pedantic --ansi
GSL =
GSL_LIB = ./libgsl.a ./libgslcblas.a
RM = rm
MKDIR = mkdir
CP = cp
EXECUTABLE_NAME = SPEM
DEBUG_EXECUTABLE_NAME = SPEM_dbg

FILE = $*

all: src/Model.o src/Calendar.o src/Parse.o src/input.o src/Calculator.o
	$(CPP) $(GSL) $(INCLUDE) SPEM.cpp $^ -o $(EXECUTABLE_NAME) $(GSL_LIB)

dbg: src/Model_dbg.o src/Calendar_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o
	$(CPP) $(DEBUG) $(INCLUDE) $(GSL) SPEM.cpp $^ -o $(DEBUG_EXECUTABLE_NAME) $(GSL_LIB)

%_dbg.o: %.cpp
	$(CPP) -c $(DEBUG) $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE)_dbg.o

%.o: %.cpp
	$(CPP) -c $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE).o

test_dir:
	$(MKDIR) runs\multi\1247499707714.A0C\output
	$(MKDIR) runs\single\1247498152606.1EE\output

	$(CP) input\simul.* runs\multi\1247499707714.A0C\
	$(CP) input\req_multi.cfg runs\multi\1247499707714.A0C\req.cfg

	$(CP) input\simul.* runs\single\1247498152606.1EE\
	$(CP) input\req_single.cfg runs\single\1247498152606.1EE\req.cfg

clean:
	$(RM) -f src/*.o
	$(RM) -f $(EXECUTABLE_NAME)
	$(RM) -f $(DEBUG_EXECUTABLE_NAME)
	$(RM) -f runs

clean_run:
	$(RM) -f runs
