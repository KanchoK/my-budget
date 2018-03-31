package uni.fmi.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {
    private static final Logger LOG = Logger.getLogger(ConfigUtil.class);
    private static final String CONFIG_PROPERTIES = "config.properties";

    public static Map<String, Object> getLoadedProps() {
        HashMap<String, Object> props = new HashMap<>();

        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(input);
            properties.stringPropertyNames()
                    .forEach(n -> props.put(n, properties.getProperty(n)));
        } catch (IOException e) {
            LOG.error("Exception was thrown", e);
        }

        return props;
    }
}
