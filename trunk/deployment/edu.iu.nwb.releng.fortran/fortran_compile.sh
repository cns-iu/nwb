#!/bin/bash
set -e

if [ ! $# == 1 ]
then
    echo "fortran_compile.sh <PLATFORM TO COMPILE FOR>"
    exit -1
fi

PLATFORM_TO_USE="$1"

#win32 does not work currently.. I can't get g95 to actually compile stuff...
if [ $PLATFORM_TO_USE == "win32" ]
then
	PLATFORM=win32
	BINARY=/y/win32/sfw/g95/bin/g95
fi

if [ $PLATFORM_TO_USE == "linux.x86_64" ]
then 
	PLATFORM=linux.x86_64
	BINARY=`pwd`/g95/g95-install_$PLATFORM/bin/x86_64-unknown-linux-gnu-g95
fi

if [ $PLATFORM_TO_USE == "linux.x86" ]
then 
	PLATFORM=linux.x86
	BINARY=`pwd`/g95/g95-install_$PLATFORM/bin/i686-suse-linux-gnu-g95
fi

if [ $PLATFORM_TO_USE == "solaris.sparc" ]
then
	PLATFORM=solaris.sparc
	BINARY=`pwd`/g95/g95-install_$PLATFORM/bin/sparc-sun-solaris2.9-g95
fi

if [ $PLATFORM_TO_USE == "macosx.x86" ]
then 
	PLATFORM=macosx.x86
	BINARY=`pwd`/g95/g95-install_$PLATFORM/bin/i386-apple-darwin8.11.1-g95
fi

if [ $PLATFORM_TO_USE == "macosx.ppc" ]
then
    PLATFORM=macosx.ppc
    BINARY=`pwd`/g95/g95-install_$PLATFORM/bin/powerpc-apple-darwin6.8-g95
fi


mkdir -p build

echo "Checking out fortran algorithms..."
cat algorithms.csv | while read line
do
	TYPE=`echo $line | cut -d"," -f 1`
	X=`echo $line | cut -d"," -f 2`
	if [ ! -e "build/$X" ]
	then
		svn co svn+ssh://cns-nd3.slis.indiana.edu/projects/svn/nwb/trunk/plugins/$TYPE/$X build/$X
	fi
done


echo "Compiling fortran algorithms for the $PLATFORM platform..."
cat algorithms.csv | while read line
do
	TYPE=`echo $line | cut -d"," -f 1`
	X=`echo $line | cut -d"," -f 2`
	cd build/$X/src

	src=`ls *.f90`
	alg_name=${src%.f90}
	mkdir -p "../ALGORITHM/$PLATFORM"
	echo "$BINARY $src -O3 -o ../ALGORITHM/$PLATFORM/$alg_name"
	$BINARY $src -O3 -o ../ALGORITHM/$PLATFORM/$alg_name

	cd ../../../
done
