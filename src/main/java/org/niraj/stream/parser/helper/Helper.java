package org.niraj.stream.parser.helper;

import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.constants.DateTimeConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Helper {


    private static final String DEBUG_MODE_PROPERTY = "debug";
    public static final String DATA_TYPE_KEY = "data-type";
    private static final String DATA_ROOT_DIRECTORY = "data";
    private String DATA_DIRECTORY;
    private static Helper instance = null;


    private Map<String, Map<String, Integer>> timeBuckets;

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    private Helper() {
        _setTimeBucket();
    }

    public void setDataDirectory(String dataDirectory) {
        DATA_DIRECTORY = dataDirectory;
    }

    public String getAbsolutePath(String file, Boolean isData) throws IOException {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int jarIndex = path.lastIndexOf(File.separator);
        if (isData) {
            if (DATA_DIRECTORY == null)
                throw new IOException("Data directory is not set");
            return path.substring(0, jarIndex) + File.separator + DATA_ROOT_DIRECTORY + File.separator + DATA_DIRECTORY + File.separator + file;
        } else {
            return path.substring(0, jarIndex) + File.separator + file;
        }

    }

    public String getTimeInFuture(String format, int timeToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, timeToAdd);

        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public boolean isDebugMode(ConfigReader configReader) {
        String debug = configReader.getProperty(DEBUG_MODE_PROPERTY);
        return new Boolean(debug);
    }

    public String getTimeBucket(String dateTime) {
        LocalDateTime date = parseDate(dateTime);
        int hour = date.getHour();
        String bucket = "N/A";
        if (hour >= timeBuckets.get(DateTimeConstants.EARLY_MORNING).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.EARLY_MORNING).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.EARLY_MORNING;
        } else if (hour >= timeBuckets.get(DateTimeConstants.MORNING).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.MORNING).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.MORNING;
        } else if (hour >= timeBuckets.get(DateTimeConstants.AFTERNOON).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.AFTERNOON).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.AFTERNOON;
        } else if (hour >= timeBuckets.get(DateTimeConstants.EVENING).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.EVENING).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.EVENING;
        } else if (hour >= timeBuckets.get(DateTimeConstants.NIGHT).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.NIGHT).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.NIGHT;
        } else if (hour >= timeBuckets.get(DateTimeConstants.LATE_NIGHT).get(DateTimeConstants.LOW_KEY)
                && hour < timeBuckets.get(DateTimeConstants.LATE_NIGHT).get(DateTimeConstants.HIGH_KEY)) {
            bucket = DateTimeConstants.LATE_NIGHT;
        }

        return bucket;
    }

    public LocalDateTime parseDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.S");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public String getDayBucket(String dateTime) {
        LocalDateTime date = parseDate(dateTime);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        String dayBucket;

        switch (dayOfWeek) {
            case SATURDAY:
                dayBucket = DateTimeConstants.WEEKEND;
                break;
            case SUNDAY:
                dayBucket = DateTimeConstants.WEEKEND;
                break;
            default:
                dayBucket = DateTimeConstants.WEEKDAYS;
        }

        return dayBucket;

    }

    private void _setTimeBucket() {
        timeBuckets = new HashMap<>();
        timeBuckets.put(DateTimeConstants.EARLY_MORNING, new HashMap<>());
        timeBuckets.get(DateTimeConstants.EARLY_MORNING).put(DateTimeConstants.LOW_KEY, 4);
        timeBuckets.get(DateTimeConstants.EARLY_MORNING).put(DateTimeConstants.HIGH_KEY, 8);

        timeBuckets.put(DateTimeConstants.MORNING, new HashMap<>());
        timeBuckets.get(DateTimeConstants.MORNING).put(DateTimeConstants.LOW_KEY, 8);
        timeBuckets.get(DateTimeConstants.MORNING).put(DateTimeConstants.HIGH_KEY, 12);

        timeBuckets.put(DateTimeConstants.AFTERNOON, new HashMap<>());
        timeBuckets.get(DateTimeConstants.AFTERNOON).put(DateTimeConstants.LOW_KEY, 12);
        timeBuckets.get(DateTimeConstants.AFTERNOON).put(DateTimeConstants.HIGH_KEY, 16);

        timeBuckets.put(DateTimeConstants.EVENING, new HashMap<>());
        timeBuckets.get(DateTimeConstants.EVENING).put(DateTimeConstants.LOW_KEY, 16);
        timeBuckets.get(DateTimeConstants.EVENING).put(DateTimeConstants.HIGH_KEY, 20);

        timeBuckets.put(DateTimeConstants.NIGHT, new HashMap<>());
        timeBuckets.get(DateTimeConstants.NIGHT).put(DateTimeConstants.LOW_KEY, 20);
        timeBuckets.get(DateTimeConstants.NIGHT).put(DateTimeConstants.HIGH_KEY, 24);

        timeBuckets.put(DateTimeConstants.LATE_NIGHT, new HashMap<>());
        timeBuckets.get(DateTimeConstants.LATE_NIGHT).put(DateTimeConstants.LOW_KEY, 0);
        timeBuckets.get(DateTimeConstants.LATE_NIGHT).put(DateTimeConstants.HIGH_KEY, 4);
    }
}
