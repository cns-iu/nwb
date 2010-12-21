on run argv
	set x to item 1 of argv
	set dateVar to current date
	set dateVar to (year of dateVar) & (month of dateVar) & (day of dateVar) & (time of dateVar)
	set fileName to "/tmp/gnuplotstart.txt" & (dateVar) & ""
	set fp to open for access POSIX file fileName with write permission
	write to fp
	write "plot " to fp
	write "\"" & x & "\"" to fp
	close access fp
	tell application "Terminal"
		do script "gnuplot -persist " & fileName & " -"
	end tell
end run