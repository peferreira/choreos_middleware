package eu.choreos.monitoring.platform.daemon;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.cnr.isti.labse.glimpse.event.GlimpseBaseEventImpl;
import it.cnr.isti.labse.glimpse.utils.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import eu.choreos.monitoring.platform.daemon.notifier.GlimpseMessageHandler;
import eu.choreos.monitoring.platform.daemon.notifier.MessageHandlingFault;
import eu.choreos.monitoring.platform.exception.GangliaException;

public class ThresholdEvalDaemonTest {

	private ThresholdEvalDaemon daemon;
	private GlimpseBaseEventImpl<String> message;
	private GlimpseMessageHandler msgHandler;
	private String javaNamingProviderUrl;
	
	
	private ThresholdManager thresholdManager;

	@Before
	public void setUp() throws Exception {
		thresholdManager = mock(ThresholdManager.class);
		when(thresholdManager.thereAreSurpassedThresholds()).thenReturn(true);
		
		List<AbstractThreshold> host1ThresholdsList = new ArrayList<AbstractThreshold>();
		host1ThresholdsList.add(new SingleThreshold("load_one", SingleThreshold.MAX, 3));
		host1ThresholdsList.add(new SingleThreshold("load_five", SingleThreshold.MAX, 3));
		
		Map<String, List<AbstractThreshold>> thresholdsMap = new HashMap<String, List<AbstractThreshold>>();
		thresholdsMap.put("host1", host1ThresholdsList);
		
		when(thresholdManager.getSurpassedThresholds()).thenReturn(thresholdsMap);
		
		
		javaNamingProviderUrl = "tcp://dsbchoreos.petalslink.org:61616";
		message = getDefaultMessage();
		msgHandler = mock(GlimpseMessageHandler.class);
		daemon = new ThresholdEvalDaemon(getProperties(), "localhost", 8649,
				msgHandler, thresholdManager);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldSendAllThresholdsMessage() throws GangliaException, MessageHandlingFault {
		daemon.addThreshold(new SingleThreshold("load_one", SingleThreshold.MAX, 1.0));
		daemon.addThreshold(new SingleThreshold("load_five", SingleThreshold.MIN, 1.0));
		
		daemon.evaluateThresholdsSendMessagesAndSleep(message, 0);
		
		verify(msgHandler, times(2)).sendMessage(
				any(GlimpseBaseEventImpl.class));
	}

	
	private GlimpseBaseEventImpl<String> getDefaultMessage() {
		return new GlimpseBaseEventImpl<String>("thresholdAlarm", "connector1",
				"connInstance1", "connExecution1", 1, 2,
				System.currentTimeMillis(), "NS1", false);
	}
	
	private Properties getProperties() {
		Properties createProbeSettingsPropertiesObject = Manager
				.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						javaNamingProviderUrl, "system", "manager",
						"GangliaFactory", "jms.probeTopic", true, "probeName",
						"probeTopic");
		return createProbeSettingsPropertiesObject;
	}
	
}
