# 06/17/2011 Static compilation for Windows is not working. So for now we have lib*.dll files
# alongside the executables in the repository.
CPP_32 = g++ -m32 -march=i386 -static
INCLUDE32 = -Iinclude/ -I./
DEBUG = -g -Wall -Wextra --pedantic --ansi 
GSL_LIB32 = -L./ -lgsl -lgslcblas 
RM = del
MKDIR = mkdir
CP = copy

SPEM_EXECUTABLE = SPEM.exe
SPEM_DEBUG_EXECUTABLE = SPEM_dbg.exe
EXACTEM_EXECUTABLE = ExactEM.exe
EXACTEM_DEBUG_EXECUTABLE = ExactEM_dbg.exe
NETEM_EXECUTABLE = NetEM.exe
NETEM_DEBUG_EXECUTABLE = NetEM_dbg.exe

FILE = $*

all: SPEM32 ExactEM32 NetEM32

NetEM32: src/ModelNetwork32.o src/Parse32.o src/input32.o src/Graph32.o src/Population32.o src/Calculator32.o src/Output32.o src/Calendar32.o NetEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(NETEM_EXECUTABLE)

ExactEM32: src/ExactModel32.o src/Parse32.o src/input32.o src/Calculator32.o ExactEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(EXACTEM_EXECUTABLE)

SPEM32: src/Output32.o src/Model32.o src/Calendar32.o src/Parse32.o src/input32.o src/Calculator32.o SPEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(SPEM_EXECUTABLE)
	
dbg: SPEM_dbg ExactEM_dbg NetEM_dbg

archive:
	git archive --format=tar --prefix=EpiC/ EpiC > EpiC-1.0.tar
	tar -rvf EpiC-1.0.tar ../EpiC/ShellFramework/*.py
	gzip EpiC-1.0.tar

NetEM32_dbg: src/ModelNetwork32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Graph32_dbg.o src/Population32_dbg.o src/Calculator32_dbg.o src/Output32_dbg.o src/Calendar32_dbg.o NetEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(NETEM_EXECUTABLE)
	
SPEM32_dbg: src/Output32_dbg.o src/Model32_dbg.o src/Calendar32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Calculator32_dbg.o SPEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(SPEM_EXECUTABLE)

ExactEM32_dbg: src/ExactModel32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Calculator32_dbg.o ExactEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(EXACTEM_EXECUTABLE)

%32_dbg.o: %.cpp
	$(CPP_32) -c $(DEBUG) $(INCLUDE32) $(GSL) $(FILE).cpp -o $(FILE)32_dbg.o

%32.o: %.cpp
	$(CPP_32) -c $(INCLUDE32) $(FILE).cpp -o $(FILE)32.o

%ppc.o: %.cpp
	$(CPP_PPC) -c $(INCLUDE32) $(FILE).cpp -o $(FILE)ppc.o

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
	$(RM) src\*.o
	$(RM) *.exe 
	$(RM) runs\

clean_run:
	$(RM) runs\