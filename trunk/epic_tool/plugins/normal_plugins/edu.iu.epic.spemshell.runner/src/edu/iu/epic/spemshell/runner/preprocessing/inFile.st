group inFile;

inFile(modelFileName,numberOfSecondaryEvents,population,susceptibleCompartmentID,compartmentPopulations,numberOfDays,date,seed) ::= <<
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

# What is the label for the susceptible compartment?
susceptible <susceptibleCompartmentID>

# initial compartment populations
<compartmentPopulations; separator="\n">

# number of days
days <numberOfDays>

# First day of the infection
date <date>

# run output directory
output .

# Random number generator seed
seed <seed>

###############################################################################
# End Input File                                                              #
###############################################################################
>>