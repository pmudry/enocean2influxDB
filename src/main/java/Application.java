import org.opencean.core.ESP3Host;
import org.opencean.core.EnoceanReceiver;
import org.opencean.core.EnoceanSerialConnector;
import org.opencean.core.address.EnoceanId;
import org.opencean.core.common.ProtocolConnector;
import org.opencean.core.packets.BasicPacket;
import org.opencean.core.packets.QueryIdCommand;
import org.opencean.core.packets.RadioPacket4BS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sensors.STM330;
import utils.Sensors;

public class Application {

	
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	Application(String port) {
		logger.info("starting..");
		ProtocolConnector connector = new EnoceanSerialConnector();
		connector.connect(port);
		ESP3Host esp3Host = new ESP3Host(connector);
		BasicPacket packet = new QueryIdCommand();
		esp3Host.sendRadio(packet);
		esp3Host.start();

		esp3Host.addListener(new EnoceanReceiver() {
			public void receivePacket(BasicPacket packet) {
				if (packet instanceof RadioPacket4BS) {
					logger.info("Got 4BS packet");
					
					RadioPacket4BS p4bs = (RadioPacket4BS) packet;
					EnoceanId gotId = p4bs.getSenderId();
					
					if(Sensors.sensors.containsKey(gotId)) {
						
						if(p4bs.getDb0() == (byte)0x80){
							logger.info("Got existing sensor with learn bit set");
						}
						else
						{
							logger.info("Got existing sensor with actual temperature (value " + p4bs.getDb1() + ")");
							STM330 sensor = Sensors.sensors.get(gotId);
							sensor.assignTemperature(p4bs.getDb1());
							logger.info(sensor.toString());
						}
					}
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			new Application("/dev/ttyUSB0");
		else
			new Application(args[0]);
	}
}
