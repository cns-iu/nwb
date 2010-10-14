#!/usr/bin/env bash

for i in $@; do FILE=`echo $i | awk -F "." '{printf("%s.",$1); for(i=2;i<NF;++i)printf("%s.",$i);print"gif"}'`; echo $i "->" $FILE; python/dotify.py $i | dot -Tsvg -o /tmp/temp.svg; convert /tmp/temp.svg $FILE; done
