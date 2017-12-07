package org.niraj.stream.parser.helper;

import org.niraj.stream.parser.configuration.ConfigReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    private static final String DEBUG_MODE_PROPERTY = "debug";
    private static String DATA_DIRECTORY = "data";
    private static Helper instance = null;

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    private Helper() {
    }

    public String getAbsolutePath(String file, Boolean isData) {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int jarIndex = path.lastIndexOf(File.separator);
        if (isData) {
            return path.substring(0, jarIndex) + File.separator + DATA_DIRECTORY + File.separator + file;
        } else {
            return path.substring(0, jarIndex) + File.separator + file;
        }

    }

    public String getTimeInFuture(String format, int timeToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, timeToAdd);

        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public boolean isDebugMode(ConfigReader config) {
        String debug = config.getProperty(DEBUG_MODE_PROPERTY);
        return new Boolean(debug);
    }
}
