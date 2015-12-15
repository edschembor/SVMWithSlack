import csv
import sys
from textblob import TextBlob

target = open("CleanedFileMulti.dev", 'w')
target2 = open("DataDictionary", "w")

noFormatting = [2, 3, 4, 7, 13, 24, 25, 30]

first = 1
one_count = 0
zero_count = 0

with open(sys.argv[1], 'rb') as csvFile:
    reader = csv.reader(csvFile, delimiter=",")
    next(reader, None)
    next(reader, None)
    for row in reader:

        if("Paid" in row[16]):
            if(one_count < zero_count):
                one_count+=1
                target.write("1 ")
            else:
                continue
        elif("Charged" in row[16]):
            zero_count+=1
	    target.write("0 ")
        else:
	    continue

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
                elif("n" in row[index]):
                    target.write(str(index) + ":" + "0")
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
                if(year > 16):
                    delta = 115 - year
                else:
                    delta = 15-year
                target.write(str(index) + ":" + str(delta))
                target.write(" ")

            if index == 33:
                val = row[index].rstrip("%")
                if(str(val) == ""):
                    val = 0
                target.write(str(index) + ":" + str(val))
                target.write(" ")

            if index == 19:
                # Pass description into TextBlob
                try:
                    description = TextBlob(row[index])
                    if description.sentiment.polarity < 0:
                        target.write("51:-100 ")
                    elif description > 0:
                        target.write("51:100 ")
                    else:
                        target.write("51:0 ")

                except UnicodeDecodeError:
                    target.write("51:0 ")

        target.write("50:10 ")
        target.write("12:" + str(row[12]))

        target.write("\n")
