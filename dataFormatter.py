import csv
import sys


target = open("CleanedFile.dev", 'w')
target2 = open("DataDictionary", "w")

noFormatting = [2, 3, 4, 7, 13, 24, 25, 30]

first = 1

with open(sys.argv[1], 'rb') as csvFile:
    reader = csv.reader(csvFile, delimiter=",")
    next(reader, None)
    next(reader, None)
    for row in reader:
        for index in xrange(0, len(row)):

            # Immedietly grab the values which do not need formatting.
            if index in noFormatting:
                target.write(str(index) + ":" + row[index].strip())
                target.write(" ")

            if index == 5:
                target.write(str(index) + ":" + row[index].lstrip().split(" ")[0])
                target.write(" ")

            if index  == 11:
                if("<" in row[index]):
                    target.write(str(index) + ":" + "0")
                elif("+" in row[index]):
                    target.write(str(index) + ":" + "11")
                else:
                    target.write(str(index) + ":" + row[index].split(" ")[0])
                target.write(" ")

            if index == 14:
                if("not" in row[index].lower()):
                    target.write(str(index) + ":0 ")
                else:
                    target.write(str(index) + ":1 ")

            if index == 26:
                year = int(row[index].split("-")[1][-2:])
                print year
                if(year > 16):
                    delta = 115 - year
                else:
                    delta = 15-year
                target.write(str(index) + ":" + str(delta))
                target.write(" ")

            if index == 33:
                val = row[index].rstrip("%")
                target.write(str(index) + ":" + str(val))


        target.write("\n")
