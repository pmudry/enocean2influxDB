package apps;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.opencean.core.ESP3Host;
import org.opencean.core.EnoceanReceiver;
import org.opencean.core.EnoceanSerialConnector;
import org.opencean.core.address.EnoceanId;
import org.opencean.core.common.ProtocolConnector;
import org.opencean.core.packets.BasicPacket;
import org.opencean.core.packets.RadioPacket4BS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sensors.STM330;
import utils.Constants;
import utils.Sensors;

/**
 * Forwards Enocean packets from registered STM3330 sensors to an InfluxDB
 * database.
 * 
 * @author Pierre-André Mudry
 * @version 0.1.1
 *
 */
public class Enocean2Influx {
	private static Logger logger = LoggerFactory.getLogger(Enocean2Influx.class);

	boolean testDB = false;
	
	ESP3Host esp3Host;
	InfluxDB influxDB;

	/**
	 * Initializes the serial port handler and launches the packet handler
	 * thread
	 * 
	 * @param port
	 */
	private void enoceanInit(String port) {
		// Serial connection to USB300
		ProtocolConnector connector = new EnoceanSerialConnector();
		connector.connect(port);

		// Handler thread
		esp3Host = new ESP3Host(connector);
		esp3Host.start();
	}

	/**
	 * Connects to the InfluxDB database for time series
	 */
	private void influxInit() {
		influxDB = InfluxDBFactory.connect(Constants.INFLUX_HOST, Constants.INFLUX_USER, Constants.INFLUX_PW);
		String dbName = Constants.INFLUX_DBNAME;

		if (!influxDB.databaseExists(dbName)) {
			logger.error("Database " + dbName + " not existing. Creating");
			influxDB.createDatabase(dbName);
		}

		// Flush every 2000 Points, at least every 100ms
		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);

		logger.info(influxDB.version());
	}

	private void updateInflux(STM330 s) {
		logger.info("Sending data to influx : " + s);

		BatchPoints batchPoints = BatchPoints.database(Constants.INFLUX_DBNAME).tag("async", "true")
				.retentionPolicy("autogen").consistency(InfluxDB.ConsistencyLevel.ALL).build();

		Point point1 = Point.measurement("temperature").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("location", s.getLocation())
				.tag("id", s.getID())
				.addField("temperature", s.getTemperature()).build();

		batchPoints.point(point1);

		// Write the point to InfluxDB
		influxDB.write(batchPoints);
	}

	/**
	 * Packet listener method which filters only STM330 packets that are
	 * registered
	 * 
	 * @param packet
	 * @return
	 */
	private STM330 handleEnoceanPacket(BasicPacket packet) {
		STM330 result = null;

		/**
		 * As we may have other sensors, keep only the 4BS messages
		 */
		if (packet instanceof RadioPacket4BS) {
			logger.info("Got 4BS packet");

			RadioPacket4BS p4bs = (RadioPacket4BS) packet;
			EnoceanId gotId = p4bs.getSenderId();

			/**
			 * Keep only the messages from the registered sensors
			 */
			if (Sensors.sensors.containsKey(gotId)) {

				// Ignore sensors learning
				if (p4bs.getDb0() == (byte) 0x80) {
					logger.info("Got existing sensor with learn bit set");
				} else {
					logger.info("Got existing sensor with actual temperature (value " + p4bs.getDb1() + ")");
					STM330 sensor = Sensors.sensors.get(gotId);
					sensor.assignTemperature(p4bs.getDb1());
					logger.info(sensor.toString());
					result = sensor;
				}
			}
		}

		return result;
	}

	/**
	 * 
	 */
	private void testDB() {
		STM330 test = Sensors.sensors.get(new EnoceanId(Sensors.dummy));
		
		try {
			for (int i = 0; i < 100; i++) {
				test.assignTemperature((byte) ((byte) 0x89 + (byte) (Math.random() * 10)));
				logger.info("Sending dummy data to influx");
				updateInflux(test);
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param port
	 *            The port to listen to (given as a "/dev/ttyXXX" device)
	 */
	Enocean2Influx(String port) {
		enoceanInit(port);
		influxInit();

		if (testDB) {
			testDB();
		}

		/**
		 * Register the listener method to the Enocean packet handler
		 */
		esp3Host.addListener(new EnoceanReceiver() {
			public void receivePacket(BasicPacket packet) {
				STM330 s = handleEnoceanPacket(packet);

				if (s != null)
					updateInflux(s);
			}
		});
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			new Enocean2Influx("/dev/ttyUSB0");
		else
			new Enocean2Influx(args[0]);
	}

}
