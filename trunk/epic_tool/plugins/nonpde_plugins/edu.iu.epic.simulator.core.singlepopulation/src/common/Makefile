CPP = g++
INCLUDE = -s -O2 -static -Iinclude/
DEBUG = -g -Wall -Wextra --pedantic --ansi 
GSL = 
GSL_LIB = ./libgsl.a ./libgslcblas.a
RM = rm
MKDIR = mkdir
CP = cp
SPEM_EXECUTABLE = SPEM.exe
SPEM_DEBUG_EXECUTABLE = SPEM_dbg.exe
EXACTEM_EXECUTABLE = ExactEM.exe
EXACTEM_DEBUG_EXECUTABLE = ExactEM_dbg.exe
NETEM_EXECUTABLE = NetEM.exe
NETEM_DEBUG_EXECUTABLE = NetEM_dbg.exe

FILE = $*

all: SPEM ExactEM NetEM

NetEM: src/ModelNetwork.o src/Parse.o src/input.o src/Graph.o src/Population.o src/Calculator.o src/Output.o src/Calendar.o
	$(CPP) $(INCLUDE) $(GSL) NetEM.cpp $^ -o $(NETEM_EXECUTABLE) $(GSL_LIB)

ExactEM: src/ExactModel.o src/Parse.o src/input.o src/Calculator.o
	$(CPP) $(INCLUDE) $(GSL) ExactEM.cpp $^ -o $(EXACTEM_EXECUTABLE) $(GSL_LIB)
	

SPEM: src/Output.o src/Model.o src/Calendar.o src/Parse.o src/input.o src/Calculator.o 
	$(CPP) $(INCLUDE) $(GSL) SPEM.cpp $^ -o $(SPEM_EXECUTABLE) $(GSL_LIB)


dbg: SPEM_dbg ExactEM_dbg

archive:
	git archive --format=tar --prefix=EpiC/ EpiC > EpiC-1.0.tar
	tar -rvf EpiC-1.0.tar ../EpiC/ShellFramework/*.py
	gzip EpiC-1.0.tar

NetEM_dbg: src/ModelNetwork_dbg.o src/Parse_dbg.o src/input_dbg.o src/Graph_dbg.o src/Population_dbg.o src/Calculator_dbg.o src/Output_dbg.o src/Calendar_dbg.o
	$(CPP) $(DEBUG) $(INCLUDE) $(GSL) NetEM.cpp $^ -o $(NETEM_DEBUG_EXECUTABLE) $(GSL_LIB)
	
SPEM_dbg: src/Output_dbg.o src/Model_dbg.o src/Calendar_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o 
	$(CPP) $(DEBUG) $(INCLUDE) $(GSL) SPEM.cpp $^ -o $(SPEM_DEBUG_EXECUTABLE) $(GSL_LIB)

ExactEM_dbg: src/ExactModel_dbg.o src/Parse_dbg.o src/input_dbg.o src/Calculator_dbg.o
	$(CPP) $(DEBUG) $(INCLUDE) $(GSL) ExactEM.cpp $^ -o $(EXACTEM_DEBUG_EXECUTABLE) $(GSL_LIB)

%_dbg.o: %.cpp
	$(CPP) -c $(DEBUG) $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE)_dbg.o

%.o: %.cpp
	$(CPP) -c $(INCLUDE) $(GSL) $(FILE).cpp -o $(FILE).o

test_dir:
	$(MKDIR) -p runs/multi/1247499707714.A0C/output
	$(MKDIR) -p runs/single/1247498152606.1EE/output
	$(MKDIR) -p runs/network/2346246152606.1EE/output

	$(CP) input/simul.in runs/multi/1247499707714.A0C/
	$(CP) input/simul.mdl runs/multi/1247499707714.A0C/
	$(CP) input/req_multi.cfg runs/multi/1247499707714.A0C/req.cfg

	$(CP) input/simul.in runs/single/1247498152606.1EE/
	$(CP) input/simul.mdl runs/single/1247498152606.1EE/
	$(CP) input/req_single.cfg runs/single/1247498152606.1EE/req.cfg

	$(CP) input/simul-net.in runs/network/2346246152606.1EE/simul.in
	$(CP) input/BA.dat runs/network/2346246152606.1EE/
	$(CP) input/simul.mdl runs/network/2346246152606.1EE/
	$(CP) input/req_multi.cfg runs/network/2346246152606.1EE/req.cfg

clean:
	$(RM) -f src/*.o
	$(RM) -f *$(SPEM_EXECUTABLE) 
	$(RM) -f *$(EXACTEM_EXECUTABLE)
	$(RM) -f *$(NETEM_EXECUTABLE)
	$(RM) -f *$(SPEM_DEBUG_EXECUTABLE)
	$(RM) -rf *$(EXACTEM_DEBUG_EXECUTABLE)
	$(RM) -rf *$(NETEM_DEBUG_EXECUTABLE)
	$(RM) -rf runs/

clean_run:
	$(RM) -rf runs/
