#! /bin/bash

cvlc v4l2:///dev/video0 v4l2-standard= :input-slave=alsa://hw:0,0 :live-caching=300 :sout=#transcode{vcodec=h264,acodec=mpga,ab=128,channels=2,samplerate=44100}:rtp{sdp=rtsp://:5000/test.sdp} :sout-keep & disown
cvlc v4l2:///dev/video1 v4l2-standard= :input-slave=alsa://hw:0,0 :live-caching=300 :sout=#transcode{vcodec=h264,acodec=mpga,ab=128,channels=2,samplerate=44100}:rtp{sdp=rtsp://:5001/test.sdp} :sout-keep & disown
