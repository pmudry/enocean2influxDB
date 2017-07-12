#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "Usage : ./hexdump.sh outputfile"

else
	echo 'Logging to' $1

	stty -F /dev/ttyUSB0 57600 raw

	# We also need to set the buffer to 0, otherwise it waits for many bytes
	#stdbuf -o0 od --endian=little --width=24 -A x -t x1z -v /dev/ttyUSB0 | tee -a $1 
	stdbuf -o0 od --endian=little -A n -t x1 -v /dev/ttyUSB0 | tee -a $1 
	#od --endian=little --width=24 -A x -t x1z -v /dev/ttyUSB0 2>&1 | tee outfile.txt
fi
