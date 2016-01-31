# 2016-Stronghold
Our code for the 2016 FIRST Robotics Competition Tournament - Stronghold!

This repository contains all the code we're using for the 2016 FRC Game. All the code in this repository is subject to change at any given time.

## What's this all about
For 2016, our robot's performance is very code-focused. We're using a Kinect Camera's Infrared and Depth Streams to attain the vision tracking targets on the high goals, motion profiling to move steadily (and accurately) around the field, and
the [Pine64](http://pine64.com/) board as a Coprocessor. Our default control system consists of a 2-Joystick Adaptive Drive. All RoboRIO code is built atop the
[Toast API](https://github.com/Open-RIO/ToastAPI)

Here's a breakdown of what's responsible for what:
- RoboRIO: Central commands
    - Project Root: Drive System, Core Libraries
    - 5333-Stats: Robot statistics recording via [BlackBox](https://github.com/Open-RIO/BlackBox)
    - 5333-WebUI: Custom Web Interface to configure the Robot prior to a match and realtime visual feedback
    
- Pine64 Coprocessor: Processing-Heavy code
    - Motion Profiling (spline and trajectory generation)
    - Visual Processing (kinect + opencv)
    
TODO: Screenshots, in action, etc