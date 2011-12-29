set EDGE_CUTS=%2
set IN_FILE=%3

copy %IN_FILE% inFile.int

layout.exe -c %EDGE_CUTS% inFile
