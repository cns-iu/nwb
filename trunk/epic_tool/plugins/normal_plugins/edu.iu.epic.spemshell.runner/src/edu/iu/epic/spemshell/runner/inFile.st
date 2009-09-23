group inFile;

inFile(modelFileName,numberOfSecondaryEvents,population,susceptibleCompartmentID,compartmentPopulations,numberOfDays,seed,parameters) ::= <<
###############################################################################
# Single Population Model Input File                                          #
###############################################################################

###############################################################################
# Data Files                                                                  #
###############################################################################

# Model definition
model <modelFileName>

# Calendar
calendar airport_3378_month.txt

# Number of secondary events
# Defaults to 0
events <numberOfSecondaryEvents>

# City population
population <population>

###############################################################################
# Various                                                                     #
###############################################################################

# What is the label for the susceptible compartiment?
susceptible <susceptibleCompartmentID>

# initial compartment populations
compartment Ia 7
compartment It 3

# number of days
days <numberOfDays>

# First day of the infection
date 04/01/2007

# run output directory
output .

# Random number generator seed
seed <seed>

###############################################################################
# Parameter values                                                            #
###############################################################################

<parameters; separator="\n">

###############################################################################
# End Input File                                                              #
###############################################################################
>>