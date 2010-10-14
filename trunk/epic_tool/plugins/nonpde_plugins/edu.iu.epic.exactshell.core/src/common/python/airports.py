#!/sw/bin/python

class Airport:
    def __init__(self, line):
        
        fields = line.strip().split()

        self.id = int(fields[0])
        self.code = fields[1]
        self.population = int(fields[2])
        self.country = int(fields[3])
        self.region = int(fields[4])
        self.continent = int(fields[5])
        self.hemisphere = int(fields[6])

class Airports:
    def __init__(self, filename):
        self.file = filename
        self.data = []
        self.codes = {}

        for line in open(self.file):
            air = Airport(line.strip())
            self.data.append(air)
            self.codes[air.code] = int(air.id)

    def population(self, code):
        return self.data[self.codes[code]].population

    def hemisphere(self, code):
        return self.data[self.codes[code]].hemisphere

    def region(self, code):
        return self.data[self.codes[code]].region

    def country(self, code):
        return self.data[self.codes[code]].country

    def continent(self, code):
        return self.data[self.codes[code]].continent

