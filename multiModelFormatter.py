import csv
import sys
from textblob import TextBlob

mortgageTarget = open("MortgageModel.dev", 'w')
ownTarget = open("OwnModel.dev", "w")
rentTarget = open("RentModel.dev", "w")

noFormatting = [2, 3, 4, 7, 13, 24, 25, 30]

with open(sys.argv[1], "rb") as f:
    for line in f:
        if "12:RENT" in line:
            rentTarget.write(line.replace(" 12:RENT", ""))
        elif "12:MORTGAGE" in line:
            mortgageTarget.write(line.replace(" 12:MORTGAGE", ""))
        elif "12:OWN" in line:
            ownTarget.write(line.replace(" 12:OWN", ""))
