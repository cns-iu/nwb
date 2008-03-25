#!/bin/bash
set -e

echo "Committing changed fortran algorithms..."
cat algorithms.csv | while read line
do
	TYPE=`echo $line | cut -d"," -f 1`
	X=`echo $line | cut -d"," -f 2`
	if [ -e "build/$X" ]
	then
	    cd build/$X/ALGORITHM
        svn commit -m "Updated fortran executables"
		cd ../../../
	fi
done
