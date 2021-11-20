//Nicholas Koeppen
//Used to read properties 
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-07
 */
public class ConfigProperties {

    private static final File configFile = new File("src/main/java/files/config.properties");
    private static long autosaveInterval;
    private static boolean startFullscreen;
    private final static Logger logger = Logger.getLogger("ConfigProperties");

    /**
     * Reads the Configuration file (located in src/main/java/files/config.properties)
     */
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
            startFullscreen = Boolean.parseBoolean(properties.getProperty("startFullscreen"));
            logger.info("Configuration file read and settings applied");
        }
    }

    /**
     * Copies the Static Class Variables into the Configuration file (located in src/main/java/files/config.properties)
     */
    public static void write() {
        if (configFile.exists()) {
            Properties properties = new Properties();

            try {
                FileInputStream fileReader = new FileInputStream(configFile);
                properties.load(fileReader);
                FileOutputStream fileWriter = new FileOutputStream(configFile);
                
                //Sets config file properties
                properties.setProperty("autosaveInterval", autosaveInterval + "");
                properties.setProperty("startFullscreen", startFullscreen + "");
                
                //Writes config file properties
                properties.store(fileWriter, "Configuration");
                
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            logger.warning("Configuration file written");
        }
    }

    /**
     * Returns the Autosave Interval as a long integer
     * @return Autosave Interval as a long
     */
    public static long getAutosaveInterval() {
        return autosaveInterval;
    }

    /**
     * Sets the Autosave Variable
     * @param autosaveInterval new Autosave Interval in milliseconds
     */
    public static void setAutosaveInterval(long autosaveInterval) {
        ConfigProperties.autosaveInterval = autosaveInterval;
    }
    
    /**
     * Returns a boolean to indicate whether the program will start fullscreen
     * @return boolean to indicate whether the program will start fullscreen
     */
    public static boolean getStartFullscreen() {
        return startFullscreen;
    }
    
    /**
     * Sets whether the program will start maximized
     * @param startFullscreen boolean which is true if program should start fullscreen
     */
    public static void setStartFullscreen(boolean startFullscreen) {
        ConfigProperties.startFullscreen = startFullscreen;
    }
}
