package apps.tests;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxDeleter {

	public static void main(String[] args) {
		// Connect to InfluxDB
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");

		// Create a database
		String dbName = "temperatureMeasurements";
		influxDB.deleteDatabase(dbName);
	}

}
