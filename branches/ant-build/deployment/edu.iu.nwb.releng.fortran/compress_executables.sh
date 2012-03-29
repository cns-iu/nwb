#!/bin/bash
set -e

UPX=`pwd`/upx/i386-linux-upx
#UPX=`pwd`/upx/win32-upx

mkdir -p build

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

for PLATFORM in win32 linux.x86_64 linux.x86 macosx.x86 macosx.ppc
do
    echo "Compressing fortran algorithms for the $PLATFORM platform..."
    cat algorithms.csv | while read line
    do
        TYPE=`echo $line | cut -d"," -f 1`
    	X=`echo $line | cut -d"," -f 2`
    	cd build/$X/src

        src=`ls *.f90`
        alg_name=${src%.f90}
        
        if [ -e "../ALGORITHM/$PLATFORM/$alg_name" ]
        then
            $UPX --ultra-brute --best ../ALGORITHM/$PLATFORM/$alg_name
        fi
        
        cd ../../../
    done
done
