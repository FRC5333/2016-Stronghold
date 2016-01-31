Kinect + OpenCV Vision Tracking
----

This is the C/C++ mixed source for the Kinect + OpenCV Vision Processing and Targetting system on our Pine64 Coprocessor for the 2016 FRC Tournament "FIRST Stronghold".

## What does it do?
This set of code does a LOT of stuff, so let's try and break it down.
- Tracks high goals using Infrared and Depth streams
- Compresses Infrared, RGB and Depth Streams using LZ4 for viewing on the Driver Station (also capable of Picture in Picture)
- Calculates distance to the High Goal and desired launch velocity/angle from the Robot's main flywheel.

All this (in conjunction with the SplineLib) allows us to just hit a button once we cross the Outer Works and have the Robot drive, aim and fire
for us. Not only does this generate the shortest path possible (time is critical), but also creates accurate shots. This can become critical
as not only does it prevent our robot being pinned to a single shooting position (which can easily be blocked by defensive robots), and also means
that precious time is not wasted aligning (and retaking) shots.

It should be noticed that this directory contains mostly recognition and calculation of targets and their distances. The actual drive code for all this is
included in the Root Java Project. This exists to lift processing power off of the Robot and delegate it to a secondary processor.