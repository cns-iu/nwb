GleamProxy calls a shell script wrapper to run the simulations.
The wrapper takes care of running the executable(s) to 
produce the output files.


When calling it, GleamProxy passes a single parameter to the wrapper:
the name of the directory in which the input files will be found (a
relative subpath starting from a pre-defined base path).


The directory (let's call it SIMDIR) should contain three files called:

   req.cfg
   simul.in
   simul.mdl


The req.cfg file should contain two lines:

   RUNS: <integer>    -> 1 for single/live run, >1 for multiple runs 
   OUTVAL: <string>   -> a list of compartment labels, as defined in
			 the model, separated by ";" (semicolon)

OUTVAL defines the output quantity to be displayed on the visualization
client application.


The directory structure will be like this:

   <base path>/[multi|single]/<run id>/

   (for example the argument passed to the wrapper could be
    "single/1247498152606.1EE" or "multi/1247499707714.A0C")
    

When the EpiC tool calls the wrapper the SIMDIR will contain
only the 3 conf files (req.cfg, simul.in, simul.mdl) plus an additional 
empty directory, called:

   output_data

(see below).


The shell creates an empty file called:

   run.<pid number>.pid

inside SIMDIR as soon as the simulation is run, containing the process
id of the executable performing the actual simulation. For multiple
runs there could be more than one files following the same naming
convention. Those files will be removed by the shell as soon as the
corresponding process finishest.


The simulation engine will write inside the "output" directory (among
other things) all the output files for each step of the simulation. 
Those files will be called:

   step-YYYY-MM-DD.dat	(for aggregated data over all runs)

where YYYY, MM and DD are respectively the year, month and day of the
output step (1 digit months/days should be left padded with a "0").

The step-YYYY-MM-DD.dat files should contain six values for each
row:
    the number of new cases of the requested output variable
    the lower bound of the confidence interval (new cases)
    the upper bound of the confidence interval (new cases)
    the number of cumulative cases of the requested output variable
    the lower bound of the confidence interval (cumulative cases)
    the upper bound of the confidence interval (cumulative cases)

All values will be treated as floating point numbers.

At the end of the simulation (after the last step files have
been saved) the wrapper will create an empty file inside SIMDIR called:

   run.end

(useful, especially for multiple runs, to confirm to the toll that all
the computation is done...)

The data files of each computation step should be
saved into the "<SIMDIR>/output" directory, in order to be
accessible for further analysis/visualization. These files will contain
the number of people in each compartment at each time step and for each run.
