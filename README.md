# Enocean2InfluxDB
'
mvn install
'

should create the executable jar file. This program depends on 'openocean-core' which must be build with mvn before.

## InfluxDB installer with docker
sudo docker run -p 8086:8086 -p 8083:8083 influxdb

## InfluxDB run
sudo docker start influxdb

## Grafana install
Grafana in docker can be installed using the 'grafana.sh' script

then

sudo docker start grafana
sudo docker start grafana-storage
