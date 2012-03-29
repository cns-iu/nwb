#!/bin/bash
set -e

if [ ! $# == 1 ]
then
    echo "fortran_compile.sh <PLATFORM TO COMPILE FOR>"
    exit -1
fi
PLATFORM_TO_USE="$1"

#win32 does not work currently.. I can't get g95 to actually compile stuff...
if [ $PLATFORM_TO_USE == "win32" ]; then
	PLATFORM=win32
	BINARY=/y/win32/sfw/g95/bin/g95
	ARCHIVE=Must install manually.
	echo "Win32 not supported.. yet"
	exit -1
fi

if [ $PLATFORM_TO_USE == "linux.x86_64" ]; then 
	PLATFORM=linux.x86_64
	BINARY=x86_64-unknown-linux-gnu-g95
	ARCHIVE=g95-x86_64-64-linux.tgz
fi

if [ $PLATFORM_TO_USE == "linux.x86" ]; then 
	PLATFORM=linux.x86
	BINARY=i686-suse-linux-gnu-g95
	ARCHIVE=g95-x86-linux.tgz
fi

if [ $PLATFORM_TO_USE == "solaris.sparc" ]; then
	PLATFORM=solaris.sparc
	BINARY=sparc-sun-solaris2.10-g95
	ARCHIVE=g95-sparc-solaris.tgz
fi

if [ $PLATFORM_TO_USE == "macosx.x86" ]; then 
	PLATFORM=macosx.x86
	BINARY=i386-apple-darwin8.11.1-g95
	ARCHIVE=g95-x86-osx.tgz
fi

if [ $PLATFORM_TO_USE == "macosx.ppc" ]; then
    PLATFORM=macosx.ppc
    BINARY=powerpc-apple-darwin6.8-g95
	ARCHIVE=g95-powerpc-osx.tgz
fi


mkdir -p build
cd build
tar zxf ../g95/$ARCHIVE
G95=`pwd`/g95-install/bin/$BINARY
cd ..

echo "Checking out fortran algorithms..."
cat algorithms.csv | while read line
do
	TYPE=`echo $line | cut -d"," -f 1`
	X=`echo $line | cut -d"," -f 2`
	if [ ! -e "build/$X" ]
	then
		svn co svn+ssh://cns-nd3.slis.indiana.edu/projects/svn/nwb/branches/ant-build/plugins/$TYPE/$X build/$X
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
	echo "$G95 $src -O3 -o ../ALGORITHM/$PLATFORM/$alg_name"
	$G95 $src -O3 -o ../ALGORITHM/$PLATFORM/$alg_name

	cd ../../../
done
