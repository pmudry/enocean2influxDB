package apps.tests;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxTester {

	public static void main(String[] args) {
		Random r = new Random();

		// Connect to InfluxDB
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");

		// Create a database
		String dbName = "temperatureMeasurements";
		influxDB.createDatabase(dbName);

		// Create a 'batch' of 'points'
		BatchPoints batchPoints = BatchPoints.database(dbName).tag("async", "true").retentionPolicy("autogen")
				.consistency(InfluxDB.ConsistencyLevel.ALL).tag("BatchTag", "BatchTagValue").build();

		try {
			for (int i = 0; i < 100; i++) {
				double temp = 25 + r.nextDouble() * 2;
				Point point1 = Point.measurement("temperature").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.addField("location", "chambre").addField("id", "01:03:02:AA").addField("temperature", temp)
						.build();
				batchPoints.point(point1);
				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Write them to InfluxDB
		influxDB.write(batchPoints);

		Query query = new Query("SELECT * FROM temperature", dbName);
		QueryResult queryResult = influxDB.query(query);

		// iterate the results and print details
		for (QueryResult.Result result : queryResult.getResults()) {

			// print details of the entire result
			System.out.println(result.toString());

			// iterate the series within the result
			for (QueryResult.Series series : result.getSeries()) {
				System.out.println("series.getName() = " + series.getName());
				System.out.println("series.getColumns() = " + series.getColumns());
				System.out.println("series.getValues() = " + series.getValues());
				System.out.println("series.getTags() = " + series.getTags());
			}
		}

		// Delete the database
		// influxDB.deleteDatabase(dbName);
	}

}
