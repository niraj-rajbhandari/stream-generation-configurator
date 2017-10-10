package org.niraj.stream.generator.helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {

    public static final int MINUTE_IN_MILLIS = 60000;
    private static Helper instance = null;

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    private Helper() {
    }

    public String getAbsolutePath(String file) {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int jarIndex = path.lastIndexOf(File.separator);
        return path.substring(0, jarIndex) + File.separator + file;
    }

    public String getTimeInFuture(String format, int timeToAdd) {
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        return new SimpleDateFormat(format).format(new Date(currentMillis + (timeToAdd * MINUTE_IN_MILLIS)));
    }
}
