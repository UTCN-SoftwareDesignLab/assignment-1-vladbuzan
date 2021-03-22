package model.validation;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeGenerator {

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

    public static DateTime parse(String date) {
        return formatter.parseDateTime(date);
    }

}
