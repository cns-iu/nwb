CPP_64 = g++ -m64 -march=x86-64 -static
CPP_32 = g++ -m32 -march=i386 -static
INCLUDE32 = -Iinclude/ -I/home/bgoncalves/gsl-32/include
INCLUDE64 = -Iinclude/ -I/home/bgoncalves/gsl-64/include
DEBUG = -g -Wall -Wextra --pedantic --ansi 
GSL_LIB64 = -L/home/bgoncalves/gsl-64/lib -lgsl -lgslcblas
GSL_LIB32 = -L/home/bgoncalves/gsl-32/lib -lgsl -lgslcblas 
RM = rm
MKDIR = mkdir
CP = cp

SPEM_EXECUTABLE = SPEM
SPEM_DEBUG_EXECUTABLE = SPEM_dbg
EXACTEM_EXECUTABLE = ExactEM
EXACTEM_DEBUG_EXECUTABLE = ExactEM_dbg
NETEM_EXECUTABLE = NetEM
NETEM_DEBUG_EXECUTABLE = NetEM_dbg

FILE = $*

all: SPEM64 ExactEM64 NetEM64 SPEM32 ExactEM32 NetEM32

NetEM64: src/ModelNetwork64.o src/Parse64.o src/input64.o src/Graph64.o src/Population64.o src/Calculator64.o src/Output64.o src/Calendar64.o NetEM64.o
	$(CPP_64) $(INCLUDE64) $^ $(GSL_LIB64) -o $(NETEM_EXECUTABLE)_linux_64.x
	strip $(NETEM_EXECUTABLE)_linux_64.x

ExactEM64: src/ExactModel64.o src/Parse64.o src/input64.o src/Calculator64.o ExactEM64.o
	$(CPP_64) $(INCLUDE64) $^ $(GSL_LIB64) -o $(EXACTEM_EXECUTABLE)_linux_64.x
	strip $(EXACTEM_EXECUTABLE)_linux_64.x

SPEM64: src/Output64.o src/Model64.o src/Calendar64.o src/Parse64.o src/input64.o src/Calculator64.o SPEM64.o
	$(CPP_64) $(INCLUDE64) $^ $(GSL_LIB64) -o $(SPEM_EXECUTABLE)_linux_64.x
	strip $(SPEM_EXECUTABLE)_linux_64.x

NetEM32: src/ModelNetwork32.o src/Parse32.o src/input32.o src/Graph32.o src/Population32.o src/Calculator32.o src/Output32.o src/Calendar32.o NetEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(NETEM_EXECUTABLE)_linux_32.x
	strip $(NETEM_EXECUTABLE)_linux_32.x

ExactEM32: src/ExactModel32.o src/Parse32.o src/input32.o src/Calculator32.o ExactEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(EXACTEM_EXECUTABLE)_linux_32.x
	strip $(EXACTEM_EXECUTABLE)_linux_32.x        

SPEM32: src/Output32.o src/Model32.o src/Calendar32.o src/Parse32.o src/input32.o src/Calculator32.o SPEM32.o
	$(CPP_32) $(INCLUDE32) $^ $(GSL_LIB32) -w -o $(SPEM_EXECUTABLE)_linux_32.x
	strip $(SPEM_EXECUTABLE)_linux_32.x
	
dbg: SPEM_dbg ExactEM_dbg NetEM_dbg

archive:
	git archive --format=tar --prefix=EpiC/ EpiC > EpiC-1.0.tar
	tar -rvf EpiC-1.0.tar ../EpiC/ShellFramework/*.py
	gzip EpiC-1.0.tar

NetEM64_dbg: src/ModelNetwork64_dbg.o src/Parse64_dbg.o src/input64_dbg.o src/Graph64_dbg.o src/Population64_dbg.o src/Calculator64_dbg.o src/Output64_dbg.o src/Calendar64_dbg.o NetEM64_dbg.o
	$(CPP_64) $(DEBUG) $(INCLUDE64) $(GSL) $(GSL_LIB64) $^ -o $(NETEM_EXECUTABLE)_linux_64.x
	
NetEM32_dbg: src/ModelNetwork32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Graph32_dbg.o src/Population32_dbg.o src/Calculator32_dbg.o src/Output32_dbg.o src/Calendar32_dbg.o NetEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(NETEM_EXECUTABLE)_linux_32.x

	
SPEM64_dbg: src/Output64_dbg.o src/Model64_dbg.o src/Calendar64_dbg.o src/Parse64_dbg.o src/input64_dbg.o src/Calculator64_dbg.o SPEM64_dbg.o
	$(CPP_64) $(DEBUG) $(INCLUDE64) $(GSL) $(GSL_LIB64) $^ -o $(SPEM_EXECUTABLE)_linux_64.x
	
SPEM32_dbg: src/Output32_dbg.o src/Model32_dbg.o src/Calendar32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Calculator32_dbg.o SPEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(SPEM_EXECUTABLE)_linux_32.x

ExactEM64_dbg: src/ExactModel64_dbg.o src/Parse64_dbg.o src/input64_dbg.o src/Calculator64_dbg.o ExactEM64_dbg.o
	$(CPP_64) $(DEBUG) $(INCLUDE64) $(GSL) $(GSL_LIB64) $^ -o $(EXACTEM_EXECUTABLE)_linux_64.x

ExactEM32_dbg: src/ExactModel32_dbg.o src/Parse32_dbg.o src/input32_dbg.o src/Calculator32_dbg.o ExactEM32_dbg.o
	$(CPP_32) $(DEBUG) $(INCLUDE32) $(GSL) $(GSL_LIB32) $^ -o $(EXACTEM_EXECUTABLE)_linux_32.x

%64_dbg.o: %.cpp
	$(CPP_64) -c $(DEBUG) $(INCLUDE64) $(GSL) $(FILE).cpp -o $(FILE)64_dbg.o

%32_dbg.o: %.cpp
	$(CPP_32) -c $(DEBUG) $(INCLUDE32) $(GSL) $(FILE).cpp -o $(FILE)32_dbg.o

%64.o: %.cpp
	$(CPP_64) -c $(INCLUDE64) $(FILE).cpp -o $(FILE)64.o

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
	$(RM) -f src/*.o
	$(RM) -f *_linux_*.x 
	$(RM) -rf runs/

clean_run:
	$(RM) -rf runs/
