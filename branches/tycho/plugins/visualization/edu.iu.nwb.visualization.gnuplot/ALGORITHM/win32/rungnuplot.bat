@echo plot "%1 %2 %3 %4 %5 %6 %7 %8 %9";>gnuplotstart.txt
@awk95 "{ gsub(/\\/,\"\\\\1one1one1oneEvilControlCode\"); print }" gnuplotstart.txt>newgnuplot.txt
@awk95 "{ gsub(/1one1one1oneEvilControlCode/,\"\"); print }" newgnuplot.txt>usegnuplot.txt
wgnuplot.exe -load usegnuplot.txt -persist
