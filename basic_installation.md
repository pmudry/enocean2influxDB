# Compile instructions
To compile the Java program, simply run the following Maven command : 

```
mvn install
```

Should create the executable JAR file inside the target directory. Please note that the required other JARs files are not packed within the produced JAR but are also located within the ```target``` directory. 

The Java program depends on ```openocean-core``` which must be build with Maven before.

# Environment setup

To program on PC with linux there are the following dependencies : 

1. InfluxDB
1. Grafana
1. RXTX

## Install InfluxDB installer with docker
Should be run the first time only 
```
sudo docker run -p 8086:8086 -p 8083:8083 influxdb
```

## InfluxDB run
```
sudo docker start influxdb
```

## Grafana install
Grafana in docker can be installed using the `grafana.sh` script that is located inside the `scripts` directory.

then

```
sudo docker start grafana
sudo docker start grafana-storage
```

