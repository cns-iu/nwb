group inFile;

inFile(modelFilePath, networkFilePath, numberOfSecondaryEvents, population, susceptibleCompartmentID, numberOfDays, date, seed, outVal, initialFile, infectionsFile) ::= <<
###############################################################################
# Single Population Model Input File                                          #
###############################################################################

###############################################################################
# Data Files                                                                  #
###############################################################################

# Model definition
model <modelFilePath>

# Network file path
<if(networkFilePath)>
network <networkFilePath>
<else>
#network
<endif>


# Number of secondary events
# Defaults to 0
events <numberOfSecondaryEvents>

# City population
<if(population)>
population <population>
<else>
#population
<endif>


# Components label
outVal <outVal>

# City location
hemisphere N

###############################################################################
# Various                                                                     #
###############################################################################

# What is the label for the susceptible compartment?
susceptible <susceptibleCompartmentID>

# number of days
days <numberOfDays>

# First day of the infection
date <date>

# run output directory
output .

# Random number generator seed
seed <seed>

# Seasonality value
#alphamin 0.6

# maximum reply
maxRuns 100 

# name
name 0

# set frame
output_frames none

# initial compartment populations
initial <initialFile>

# infector seed populations
infections <infectionsFile>

###############################################################################
# End Input File                                                              #
###############################################################################
>>