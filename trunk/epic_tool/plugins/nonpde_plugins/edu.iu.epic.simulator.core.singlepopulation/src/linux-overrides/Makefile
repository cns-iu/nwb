CPP = g++
INCLUDE = -s -Iinclude/
DEBUG = -g -Wall -Wextra --pedantic --ansi 
GSL_LIB = ./libgsl.a ./libgslcblas.a
RM = rm
MKDIR = mkdir
CP = cp
SPEM_EXECUTABLE = SPEM
SPEM_DEBUG_EXECUTABLE = SPEM_dbg
EXACTEM_EXECUTABLE = ExactEM
EXACTEM_DEBUG_EXECUTABLE = ExactEM_dbg

FILE = $*

all: SPEM ExactEM

ExactEM: src/ExactModel.o src/Parse.o src/input.o src/Calculator.o
	$(CPP) $(INCLUDE) ExactEM.cpp $^ -o $(EXACTEM_EXECUTABLE) $(GSL_LIB) 
	

SPEM: src/Output.o src/Model.o src/Calendar.o src/Parse.o src/input.o src/Calculator.o 
	$(CPP) $(INCLUDE) SPEM.cpp $^ -o $(SPEM_EXECUTABLE) $(GSL_LIB)

DynaNetSPEM: src/Model.o src/Calendar.o src/Parse.o src/input.o src/Calculator.o src/DynaNet.o
	$(CPP) $(INCLUDE) NetSPEM.cpp $^ -o NetSPEM.x $(GSL_LIB)

dbg: SPEM_dbg ExactEM_dbg

archive:
	git archive --format=tar --prefix=EpiC/ EpiC > EpiC-1.0.tar
	tar -rvf EpiC-1.0.tar ../EpiC/ShellFramework/*.py
	gzip EpiC-1.0.tar

SPEM_dbg: src/Output_dbg.o src/Model_dbg.o src/Calendar_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o 
	$(CPP) $(DEBUG) $(INCLUDE) SPEM.cpp $^ -o $(SPEM_DEBUG_EXECUTABLE) $(GSL_LIB)

NetSPEM_dbg: src/Model_dbg.o src/Calendar_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o src/DynaNet_dbg.o
	$(CPP) $(DEBUG) $(INCLUDE) NetSPEM.cpp $^ -o NetSPEM_dbg.x $(GSL_LIB)

%_dbg.o: %.cpp
	$(CPP) -c $(DEBUG) $(INCLUDE) $(FILE).cpp -o $(FILE)_dbg.o

%.o: %.cpp
	$(CPP) -c $(INCLUDE) $(FILE).cpp -o $(FILE).o

test_dir:
	$(MKDIR) -p runs/multi/1247499707714.A0C/output
	$(MKDIR) -p runs/single/1247498152606.1EE/output

	$(CP) input/simul.* runs/multi/1247499707714.A0C/
	$(CP) input/req_multi.cfg runs/multi/1247499707714.A0C/req.cfg

	$(CP) input/simul.* runs/single/1247498152606.1EE/
	$(CP) input/req_single.cfg runs/single/1247498152606.1EE/req.cfg

clean:
	$(RM) -f src/*.o
	$(RM) -f *SPEM.x 
	$(RM) -f *ExactEM.x 
	$(RM) -f *ExactEM_dbg.x 
	$(RM) -rf *SPEM_dbg.x*
	$(RM) -rf runs/

clean_run:
	$(RM) -rf runs/
