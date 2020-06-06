'''
Raspberry Pi GPIO Status and Control
'''

import RPi.GPIO as GPIO
from flask import Flask, render_template, request

app = Flask(__name__)
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

relay1 = 26
ledRed = 13
ledGreen = 6
ledBlue = 5

relay1Status = 0
ledRedStatus = 0
ledGreenStatus = 0
ledBlueStatus = 0

GPIO.setup(relay1, GPIO.OUT)
GPIO.setup(ledRed, GPIO.OUT)
GPIO.setup(ledGreen, GPIO.OUT)
GPIO.setup(ledBlue, GPIO.OUT)

GPIO.output(relay1, GPIO.HIGH)
GPIO.output(ledRed, GPIO.LOW)
GPIO.output(ledGreen, GPIO.LOW)
GPIO.output(ledBlue, GPIO.LOW)

@app.route("/")
def index():
    global relay1Status, ledRedStatus, ledGreenStatus, ledBlueStatus

    relay1Status = GPIO.input(relay1)
    ledRedStatus = GPIO.input(ledRed)
    ledGreenStatus = GPIO.input(ledGreen)
    ledBlueStatus = GPIO.input(ledBlue)

    templateData = {
        '_relay1' : relay1Status,
        '_ledRed' : ledRedStatus,
        '_ledGreen' : ledGreenStatus,
        '_ledBlue' : ledBlueStatus,
    }
    return render_template('index.html', **templateData)

@app.route("/<deviceName>/<action>")    
def action(deviceName, action):
    global relay1Status, ledRedStatus, ledGreenStatus, ledBlueStatus 

    if deviceName == 'relay1':
        actuator = relay1
    elif deviceName == 'ledRed':
        actuator = ledRed
    elif deviceName == 'ledGreen':
        actuator = ledGreen
    elif deviceName == 'ledBlue':
        actuator = ledBlue

    if action == "on":
        GPIO.output(actuator, GPIO.HIGH)
    if action == "off":
        GPIO.output(actuator, GPIO.LOW)

    relay1Status = GPIO.input(relay1)
    ledRedStatus = GPIO.input(ledRed)
    ledGreenStatus = GPIO.input(ledGreen)
    ledBlueStatus = GPIO.input(ledBlue)

    templateData = {
        '_relay1' : relay1Status,
        '_ledRed' : ledRedStatus,
        '_ledGreen' : ledGreenStatus,
        '_ledBlue' : ledBlueStatus,
    }
    return render_template('index.html', **templateData)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
