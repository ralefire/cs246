package scripturefinder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Gets the properties file containing the valid topics and scriptures
 * @author Cameron Lilly
 */
public class ReadValidFiles {

    /**
     * Gets the properties file containing the valid topics and scriptures
     * @return properties file containing the valid topics and scriptures
     * @throws IOException 
     */
    public Properties getProps() throws IOException {

            Properties prop = new Properties();
            String propFileName = "config.properties";
            prop.load(getClass().getResourceAsStream("/resources/" + propFileName));
            
            if (prop == null) {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            
        return prop;
    }
}
