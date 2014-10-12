/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturefinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class ReadValidFiles {

    public Properties getProps() throws IOException {

            Properties prop = new Properties();
            String propFileName = "config.properties";
            prop.load(getClass().getResourceAsStream("/resources/" + propFileName));
            
            //BufferedReader in = new BufferedReader(new FileReader(propFileName));
            //prop.load(in);
            
            if (prop == null) {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            
        return prop;
    }
}
