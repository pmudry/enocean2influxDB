#!/bin/bash
# create /var/lib/grafana as persistent volume storage
docker start -v /var/lib/grafana --name grafana-storage busybox:latest

# start grafana
docker start \
	    -p 3000:3000 \
	      --name=grafana \
	        --volumes-from grafana-storage \
		  grafana/grafana
