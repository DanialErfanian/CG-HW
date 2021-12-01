#!/bin/bash
mkdir -p out
ffmpeg -r 25 -i 'Snaps/%04d.png' -i 'assets/music.mp3' -shortest -c:v ffv1 out/out.avi
ffmpeg -i assets/out.avi -c:v libx264 -preset veryslow -crf 28 -bufsize 10M out/out.mp4

