from flask import Flask
import RPi.GPIO as GPIO;wa


GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT)
pwm = GPIO.PWM(18, 100)
pwm.start(5)


app = Flask(__name__)

@app.route("/turn/<degree>")
def turn(degree):
    pwm.changeDutyCycle(duty)

if __name__ == '__main__':
    mapp.run()
