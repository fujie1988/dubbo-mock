#!/bin/env python
#coding=utf-8

import os
import sys
import json 
import re

def processJson(inputJsonFile, pararmMap):
    fin = open(inputJsonFile, 'r')
    fout = open("temp.txt", 'w')
    FullLine = ""
    for eachLine in fin:
        FullLine = FullLine + eachLine.replace(" ", "").replace("\n", "")

    FullLine.strip().decode('utf-8')
    
    try:
        js = json.loads(FullLine)
    except Exception,e:
        print 'bad line'

    for pararmMapKey in pararmMap:
        keyArray = pararmMapKey.split(".")
        jsObj = js

        for pos in range(len(keyArray)-1):
            key = keyArray[pos]
            if(key.find("[")>0 and key.find("]")>0):
                index = re.findall(r'\d+', key)[0] 
                jsKey = key[0:key.find("[")]
                jsObj = jsObj[jsKey][int(index)]
            else:
                jsObj = jsObj[key]

        key = keyArray[len(keyArray) - 1]
        if(key.find("[")>0 and key.find("]")>0):
            index = re.findall(r'\d+', key)[0]
            jsKey = key[0:key.find("[")]
            jsObj[jsKey][int(index)] = pararmMap[pararmMapKey]

        else:
            jsObj[key] = pararmMap[pararmMapKey]

    
    outStr = json.dumps(js, ensure_ascii = False) + ','
    fout.write(outStr.strip().replace(',','\n').encode('utf-8'))
    os.system("mv temp.txt %s" %(inputJsonFile))   
