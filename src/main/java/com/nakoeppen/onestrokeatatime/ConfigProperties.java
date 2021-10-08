//Nicholas Koeppen
//Used to read properties 
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-07
 */
public class ConfigProperties {

    private static final File configFile = new File("src/main/java/files/config.properties");
    private static long autosaveInterval;

    public static void read() {
        if (configFile.exists()) {
            Properties properties = new Properties();

            try {
                FileInputStream fileReader = new FileInputStream(configFile);
                properties.load(fileReader);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            autosaveInterval = Long.parseLong(properties.getProperty("autosaveInterval"));
        }
    }

    //Writes class variables to config file 
    public static void write() {
        if (configFile.exists()) {
            Properties properties = new Properties();

            try {
                FileInputStream fileReader = new FileInputStream(configFile);
                properties.load(fileReader);
                FileOutputStream fileWriter = new FileOutputStream(configFile);
                properties.setProperty("autosaveInterval", autosaveInterval + "");
                properties.store(fileWriter, "autosaveInterval");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    //Returns Autosave Interval in milliseconds
    public static long getAutosaveInterval() {
        return autosaveInterval;
    }

    //Sets Autosave Interval variable in milliseconds
    public static void setAutosaveInterval(long autosaveInterval) {
        ConfigProperties.autosaveInterval = autosaveInterval;
    }
}
