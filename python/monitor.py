#!/usr/bin/env python
import RPi.GPIO as GPIO
import MySQLdb
import time
db = MySQLdb.connect(host="192.168.1.2",user="user",passwd="password",db="parkydb")
cur = db.cursor()
GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(18, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
GPIO.setup(27, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
print("Monitoring...")
def s1(channel):
    if GPIO.input(17):
        cur.execute("UPDATE pongam SET S1=1 WHERE id=1000;")
        db.commit()
        print("High Detected at slot 1")
    else:
        cur.execute("UPDATE pongam SET S1=0 WHERE id=1000;")
        db.commit()
        print("Low Detected at slot 1")
def s2(channel):
    if GPIO.input(18):
        cur.execute("UPDATE pongam SET S2=1 WHERE id=1000;")
        db.commit()
        print("High Detected at slot 2")
    else:
        cur.execute("UPDATE pongam SET S2=0 WHERE id=1000;")
        db.commit()
        print("Low Detected at slot 2")
def s3(channel):
    if GPIO.input(27):
        cur.execute("UPDATE pongam SET S3=1 WHERE id=1000;")
        db.commit()
        print("High Detected at slot 3")
    else:
        cur.execute("UPDATE pongam SET S3=0 WHERE id=1000;")
        db.commit()
        print("Low Detected at slot 3")
s1(1)
s2(1)
s3(1)
GPIO.add_event_detect(17, GPIO.BOTH, callback=s1)
GPIO.add_event_detect(18, GPIO.BOTH, callback=s2)
GPIO.add_event_detect(27, GPIO.BOTH, callback=s3)
while True:
    time.sleep(0.01)
    pass
GPIO.cleanup()