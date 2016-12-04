from __future__ import division
import RPi.GPIO as GPIO
import time
import urllib2
import json
import datetime

sleep = time.sleep

# constants
classroom_id = "1"
server_url = "http://learning-zoo.herokuapp.com" # URL to server

GPIO.cleanup()
# Use the BCM GPIO numbers as the numbering scheme
GPIO.setmode(GPIO.BCM)

# Global Pin and Channel assignments and I/O functions
# Input
button = 13
GPIO.setup(button, GPIO.IN, GPIO.PUD_DOWN) # active high, default low.
# Output for DEBUG
led = 16
GPIO.setup(led, GPIO.OUT)
GPIO.output(led, GPIO.LOW)

treshold = 1 # Can set decimal for digital averaging.

def avg_input():
    # avg for debouncing. return avgs over 0.05s (5 values)
    avg = 0
    for i in range(5):
        sleep(0.01)
        avg += GPIO.input(button)
    avg = [a/5.0 for a in avg]
    return avg



# HTTP request methods
def json_request(url, data): # not used, for reference
    data = json.dumps(data)
    req = urllib2.Request(url, data, headers={'Content-Type':'application/json'})
    response = urllib2.urlopen(req)
    return response

def get_request(relative_url):
    url = server_url + relative_url
    try:
        response = urllib2.urlopen(url, timeout=10)
    except Exception as e:
        print e
        return None
    return json.loads(response)

def get_classroom():
    relative_url = "/classrooms/{}".format(classroom_id)
    return get_request(relative_url) #dunno if should put [0]

def get_session(session_id):
    relative_url = "/sessions/{}".format(session_id)
    return get_request(relative_url)

def get_topics(session_id):
    relative_url = "/sessions/{}/get_topics".format(session_id)
    return get_request(relative_url)

def increment_topic(topic_id):
    relative_url = "/topics/{}/incr_counter".format(topic_id)
    return get_request(relative_url)



# Main loop
current_session = None
session_end_time = 0
last_check = 0 # time of last check for the session end
last_pressed_topic_id = None
while True:
    if current_session is None:
        classroom =  get_classroom()
        if classroom is None: # Cannot connect. keep trying.
            continue
        current_session_id = classroom["current_session_id"]
        if current_session_id is None:
            sleep(60.0) # sleep longer
            continue
        # got a new session
        current_session = get_session(current_session_id)
        if current_session is None: # Cannot connect. keep trying.
            continue
        # Initialise end time and topic values.
        # use relative time, because pi internal clock resets.
        end_date = current_session["end_date"]
        if end_date is None:
            end_date = datetime.datetime(1970,1,1)
        start_date = current_session["start_date"]
        if start_date is None:
            start_date = datetime.datetime(1970,1,1)
        session_length = (end_date - start_date).total_seconds()
        # If session last more than 1 day, something is wrong.
        if session_length < 0 or session_length > 86400 :
            session_length = 0
        session_end_time = time.time() + session_length
        last_pressed_topic_id = None
    
    # start checking for session end 10min before predicted time
    # (if no predicted time given, will always check, because session_length=0)
    current_time = time.time()
    eta = session_end_time - current_time
    time_since_last_check = current_time - last_check
    if eta <= 600 and time_since_last_check >= 60:
        classroom =  get_classroom()
        current_session_id = None
        if classroom is not None:
            current_session_id = classroom["current_session_id"]
        if current_session["id"] != current_session_id : # session has ended.
            current_session = None
            sleep(60.0) # sleep longer
            continue
    
    # Process inputs
    button_pressed = avg_input()
    print "Averaged input: ", button_pressed, # DEBUG
    button_pressed = button_pressed >= treshold
    print " Triggered:", button_pressed # DEBUG
    GPIO.output(led, GPIO.LOW) # DEBUG
    if button_pressed:
        topics = get_topics(current_session_id)
        if topics and last_pressed_topic_id != topics[-1]["id"]:
            last_pressed_topic_id = topics[-1]["id"]
            increment_topic(last_pressed_topic_id)
            GPIO.output(led, GPIO.HIGH) # DEBUG
        sleep(2.5) # lazy blocking.
    
    sleep(0.5)
    continue