package org.ow2.choreos.enactment;

import java.io.IOException;
import java.util.Properties;

public class ThalesProperties {
	public static final String[] SERVICES_NAMES = {  };
	
	private static String PROPERTIES_FILE = "thales.properties";
	
	private static ThalesProperties INSTANCE = new ThalesProperties();

	private final Properties properties = new Properties();

    private Properties getProperties() {
        return properties;
    }

    public static String get(String key) {
        return INSTANCE.getProperties().getProperty(key);
    }

    public static void set(String key, String value) {
        INSTANCE.getProperties().setProperty(key, value);
    }

    private ThalesProperties() {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
