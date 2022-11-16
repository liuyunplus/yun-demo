#!/usr/bin/python
from sys import stdout
from time import sleep
import random
from time import strftime, localtime

# Count from 1 to 10 with a sleep
for count in range(0, 100):
s1 = random.randint(0, 1000)
s2 = strftime('%H:%M:%S',localtime())
  print(s1','s2)
  stdout.flush()
  sleep(1)
