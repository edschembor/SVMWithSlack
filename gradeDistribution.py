import sys

#index=8

predictions = open(sys.argv[1])
data = open(sys.argv[2])

pLine = predictions.readline()
dLine = data.readline()

a_1 = 0
b_1 = 0
c_1 = 0
d_1 = 0
e_1 = 0
f_1 = 0
g_1 = 0

a_0 = 0
b_0 = 0
c_0 = 0
d_0 = 0
e_0 = 0
f_0 = 0
g_0 = 0

a_true = 0;
b_true = 0;
c_true = 0;
d_true = 0;
e_true = 0;
f_true = 0;
g_true = 0;



while pLine and dLine:
    pLine = predictions.readline()
    dLine = data.readline()

    paid = dLine[0:1]
    if("1" in paid):
        if("8:A" in dLine):
            a_true+=1
        if("8:B" in dLine):
            b_true+=1
        if("8:C" in dLine):
            c_true+=1
        if("8:D" in dLine):
            d_true+=1
        if("8:E" in dLine):
            e_true+=1
        if("8:F" in dLine):
            f_true+=1
        if("8:G" in dLine):
            g_true+=1

    if("1" in pLine):
        if("8:A" in dLine):
            a_1+=1
        if("8:B" in dLine):
            b_1+=1
        if("8:C" in dLine):
            c_1+=1
        if("8:D" in dLine):
            d_1+=1
        if("8:E" in dLine):
            e_1+=1
        if("8:F" in dLine):
            f_1+=1
        if("8:G" in dLine):
            g_1+=1
    if("0" in pLine):
        if("8:A" in dLine):
            a_0+=1
        if("8:B" in dLine):
            b_0+=1
        if("8:C" in dLine):
            c_0+=1
        if("8:D" in dLine):
            d_0+=1
        if("8:E" in dLine):
            e_0+=1
        if("8:F" in dLine):
            f_0+=1
        if("8:G" in dLine):
            g_0+=1

print("A: " + str(a_1*1.0/(a_0+a_1)))
print("B: " + str(b_1*1.0/(b_0+b_1)))
print("C: " + str(c_1*1.0/(c_0+c_1)))
print("D: " + str(d_1*1.0/(d_0+d_1)))
print("E: " + str(e_1*1.0/(e_0+e_1)))
print("F: " + str(f_1*1.0/(f_0+f_1)))
print("G: " + str(g_1*1.0/(g_0+g_1)))

print("")

print("A: " + str(a_true*1.0/(a_0+a_1)))
print("B: " + str(b_true*1.0/(b_0+b_1)))
print("C: " + str(c_true*1.0/(c_0+c_1)))
print("D: " + str(d_true*1.0/(d_0+d_1)))
print("E: " + str(e_true*1.0/(e_0+e_1)))
print("F: " + str(f_true*1.0/(f_0+f_1)))
print("G: " + str(g_true*1.0/(g_0+g_1)))

predictions.close()
data.close()
