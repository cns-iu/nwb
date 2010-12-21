@ echo off
@ rem You MUST edit the following line and point GUESS_HOME to the right 
@ rem directory if you have not set it as a permanent environment variable 
@ rem (and remove the "@rem")
set GUESS_HOME=%CD%

echo 
echo Starting GUESS...
echo 
echo The initial layout for your visualization is random.  For a clearer visualization, please run a layout from the Layout menu.  (We recommend GEM.)
echo 
echo GUESS log file for this session can be found in %GUESS_HOME%\guesslog.txt

set GUESS_LIB=%GUESS_HOME%\lib
set GCLASSPATH=guess.jar;%GUESS_LIB%\guess.jar;%GUESS_LIB%\piccolo.jar;%GUESS_LIB%\piccolox.jar;%GUESS_LIB%\jung.jar;%GUESS_LIB%\commons-collections.jar;%GUESS_LIB%\hsqldb.jar;%GUESS_LIB%\freehep-all.jar;%GUESS_LIB%\colt.jar;%GUESS_LIB%\prefuse.jar;%GUESS_LIB%\TGGraphLayout.jar;%GUESS_LIB%\looks.jar;%GUESS_LIB%\mascoptLib.jar;%GUESS_LIB%\jfreechart.jar;%GUESS_LIB%\jide-components.jar;%GUESS_LIB%\jide-common.jar;%GUESS_LIB%\forms.jar;%GUESS_LIB%\jcommon.jar
java -Xms256m -Xmx1000m -classpath "%GCLASSPATH%" -Dsun.java2d.opengl=False "-Dpython.home=%GUESS_HOME%\src" -DgHome="%GUESS_HOME%" com.hp.hpl.guess.Guess %1 %2 %3 %4 %5 %6 %7 1> guesslog.txt 2>&1
