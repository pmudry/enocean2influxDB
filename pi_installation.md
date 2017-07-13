# Add InfluxDB repository to the raspbian store

```
curl -sL https://repos.influxdata.com/influxdb.key | sudo apt-key add -
source /etc/os-release
test $VERSION_ID = "7" && echo "deb https://repos.influxdata.com/debian wheezy stable" | sudo tee /etc/apt/sources.list.d/influxdb.list
test $VERSION_ID = "8" && echo "deb https://repos.influxdata.com/debian jessie stable" | sudo tee /etc/apt/sources.list.d/influxdb.list
```

# Install libfont config
```
sudo apt-get install libfontconfig1
sudo apt-get -f install
```

# Install influxdb itself
```
sudo apt-get update && sudo apt-get install influxdb
sudo service influxdb start
```

# Install Grafana8
```
cd ~
wget --output-document=grafana_4.2.0-beta1_armhf.deb https://bintray.com/fg2it/deb/download_file?file_path=testing%2Fg%2Fgrafana_4.2.0-beta1_armhf.deb
sudo dpkg -i grafana_4.2.0-beta1_armhf.deb
sudo apt-get install -f
```

## Setup that grafana starts with the PI
sudo systemctl enable grafana-server
sudo systemctl start grafana-server

reboot and then check that both services are launched

http://your.pi.address:8083
http://your.pi.address:3000

# Install JAVA and RXTX library
```
sudo apt-get install oracle-java8-jdk
sudo apt-get install librxtx-java
```

# Launch the installed JAR file with the proprer lib path
```
java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar -jar enocean2mqtt-0.1.jar
```

# Run when booting the pi
Put the `enocean2mqtt` script into `/etc/init.d`
