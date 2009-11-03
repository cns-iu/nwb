CPP = g++
INCLUDE = -Iinclude/
DEBUG = -g -Wall -Wextra --pedantic --ansi 
GSL =
GSL_LIB = ./libgsl.a ./libgslcblas.a
RM = rm
MKDIR = mkdir
CP = cp

FILE = $*

all: src/Model.o src/Calendar.o src/Parse.o src/input.o src/Calculator.o
	$(CPP) $(INCLUDE) $(GSL) SPEM.cpp $^ -o SPEM $(GSL_LIB)

dbg: src/Model_dbg.o src/Calendar_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o
	$(CPP) $(DEBUG) $(INCLUDE) $(GSL) SPEM.cpp $^ -o SPEM_dbg

%_dbg.o: %.cpp
	$(CPP) -c $(DEBUG) $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE)_dbg.o

%.o: %.cpp
	$(CPP) -c $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE).o

test_dir:
	$(MKDIR) -p runs/multi/1247499707714.A0C/output
	$(MKDIR) -p runs/single/1247498152606.1EE/output

	$(CP) input/simul.* runs/multi/1247499707714.A0C/
	$(CP) input/req_multi.cfg runs/multi/1247499707714.A0C/req.cfg

	$(CP) input/simul.* runs/single/1247498152606.1EE/
	$(CP) input/req_single.cfg runs/single/1247498152606.1EE/req.cfg

clean:
	$(RM) -f src/*.o
	$(RM) -f SPEM 
	$(RM) -rf SPEM_dbg*
	$(RM) -rf runs/

clean_run:
	$(RM) -rf runs/
