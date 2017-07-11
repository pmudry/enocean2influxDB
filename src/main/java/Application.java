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

import utils.KnownIDs;

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
					System.out.println("Is a 4bs packet");
					
					EnoceanId gotId = ((RadioPacket4BS) packet).getSenderId();
					
					if(KnownIDs.ids.contains(gotId)) {
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
