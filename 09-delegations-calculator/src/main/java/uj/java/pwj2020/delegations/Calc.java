package uj.java.pwj2020.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Calc {

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");
        Duration difference = Duration.between(
                ZonedDateTime.parse(start, dateTimeFormatter),
                ZonedDateTime.parse(end, dateTimeFormatter)
        );

        var days = difference.toDays();
        var hours = difference.minusDays(days).toHours();
        var minutes = difference.minusDays(days).minusHours(hours).toMinutes();

        var salary = dailyRate.multiply(BigDecimal.valueOf(days));

        if (hours == 0 && minutes == 0) {
            return salary;
        }

        if (hours > 12) {
            return salary.add(dailyRate);
        } else if (hours > 8) {
            return salary.add(dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        } else if (hours > 0 || minutes > 0) {
            return salary.add(dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
        } else {
            return BigDecimal.valueOf(0, 2);
        }
    }
}
