package com.revnext.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateAndTimeUtil {
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
    public static final DateTimeFormatter timeFormatterWithMinutes = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter timeFormatterWithMinutesAndAMAndPM = DateTimeFormatter.ofPattern("hh:mm a");
    public static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final DateTimeFormatter DDMMYYYY_WITH_UNDERSCORE = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    public static final DateTimeFormatter D_MMM = DateTimeFormatter.ofPattern("d MMM");
    public static final ZoneId indiaTimeZone = ZoneId.of("Asia/Kolkata");
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

    public static String formatMedium(LocalDate date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public static LocalDate getLocalDate(String dateString, DateTimeFormatter dateTimeFormatter) {
        try {
            return LocalDate.parse(dateString, dateTimeFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate getLocalDate(String dateString) {
        return getLocalDate(dateString, DDMMYYYY);
    }

    public static LocalDate getLocalDate(LocalDateTime dateString) {
        if (dateString == null) {
            return null;
        } else {
            return dateString.toLocalDate();
        }

    }

    public static LocalTime getLocalTime(String timeString, DateTimeFormatter dateTimeFormatter) {
        try {
            return LocalTime.parse(timeString, dateTimeFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime getLocalTime(String timeString) {
        timeString = timeString.trim();
        if (timeString.split(":")[0].length() == 1) {
            timeString = "0" + timeString.toLowerCase();
        } else {
            timeString = timeString.toLowerCase();
        }

        LocalTime localTime = getLocalTime(timeString, timeFormatter);
        if (localTime == null && (timeString.contains("am") || timeString.contains("pm"))) {
            return getLocalTime(timeString, timeFormatterWithMinutesAndAMAndPM);
        } else if (localTime == null) {
            return getLocalTime(timeString, timeFormatterWithMinutes);
        }

        return localTime;
    }

    public static LocalDate getToDate(LocalDate fromDate) {
        int year = fromDate.getYear();
        LocalDate cutoffDate = LocalDate.of(year, 9, 1);
        if (fromDate.isBefore(cutoffDate)) {
            return LocalDate.of(year, 12, 31);
        } else {
            return LocalDate.of(year + 1, 12, 31);
        }
    }

    public static String format(LocalDate date, DateTimeFormatter dateTimeFormatter) {
        if (Objects.isNull(date)) {
            return null;
        }
        return date.format(dateTimeFormatter);
    }

    public static String format(LocalDateTime date, DateTimeFormatter dateTimeFormatter) {
        if (Objects.isNull(date)) {
            return null;
        }
        return date.format(dateTimeFormatter);
    }

    public static int getWeekNumber(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) + 1;
    }

    public static DayOfWeek getDayOfWeek(LocalDate date) {
        return DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
    }

    public static boolean isWeekend(LocalDate localDate, List<DayOfWeek> dayOfWeeks, DayOfWeek alternateDay) {
        if (alternateDay == null) {
            DayOfWeek dayOfWeek = DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK));
            return dayOfWeeks.contains(dayOfWeek);
        } else {
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            Optional<DayOfWeek> result = dayOfWeeks.stream().filter(day -> day != alternateDay).findFirst();
            DayOfWeek otherWeekendDay = result.orElseThrow(IllegalArgumentException::new);
            if (dayOfWeek == otherWeekendDay) {
                return true;
            } else if (dayOfWeek == alternateDay) {
                LocalDate firstDayOfMonth = localDate.withDayOfMonth(1);
                LocalDate firstAlternate = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(alternateDay));
                LocalDate secondAlternate = firstAlternate.plusWeeks(1);
                LocalDate fourthAlternate = secondAlternate.plusWeeks(2);
                return localDate.equals(secondAlternate) || localDate.equals(fourthAlternate);
            }
            return false;
        }
    }

    public static Optional<DayOfWeek> getAlternateDay(List<DayOfWeek> dayOfWeeks) {
        DayOfWeek selectedDayOfWeek = null;
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            if (selectedDayOfWeek == null || (selectedDayOfWeek.getValue() > dayOfWeek.getValue()
                    || (selectedDayOfWeek.getValue() == 1 && dayOfWeek.getValue() == 7))) {
                selectedDayOfWeek = dayOfWeek;
            }
        }
        return Optional.ofNullable(selectedDayOfWeek);
    }

    public static List<LocalDate> getLocalDates(LocalDate fromDate, LocalDate toDate) {
        Objects.requireNonNull(fromDate, "From date is required");
        toDate = Objects.requireNonNullElse(toDate, fromDate);
        // Create a stream of LocalDate objects from fromDate to toDate inclusive
        return Stream.iterate(fromDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(fromDate, toDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    public static List<LocalDate> getLocalDates(LocalDate date, List<DayOfWeek> dayOfWeeks, int totalWeeks) {
        date = Objects.isNull(date) ? LocalDate.now() : date;
        List<LocalDate> localDates = new ArrayList<>();
        LocalDate nextDate = null;
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            LocalDate tempDate = date.minusDays(1).with(TemporalAdjusters.nextOrSame(dayOfWeek));
            if (nextDate == null || nextDate.isAfter(tempDate)) {
                nextDate = tempDate;
            }
        }
        if (nextDate == null) {
            nextDate = date;
        }
        for (int i = 0; i < totalWeeks * 7 + dayOfWeeks.size(); i++) {
            localDates.add(nextDate.plusDays(i));
        }
        return localDates;
    }

    public static LocalDate getLocalDate(LocalDate date, List<DayOfWeek> weekends) {
        LocalDate nextDate = null;
        for (DayOfWeek dayOfWeek : weekends) {
            LocalDate tempDate = date.minusDays(1).with(TemporalAdjusters.nextOrSame(dayOfWeek));
            if (nextDate == null || nextDate.isAfter(tempDate)) {
                nextDate = tempDate;
            }
        }
        if (nextDate == null) {
            nextDate = date;
        }
        return nextDate;
    }

    public static LocalDateTime getTimeStampToLocalDateTime(long timeStampInMillis) {
        return Instant.ofEpochSecond(timeStampInMillis).atZone(indiaTimeZone).toLocalDateTime();
    }

    public static Instant addSecondsToInstant(long seconds) {
        return Instant.now().plusSeconds(seconds);
    }

    public static Instant addSecondsToInstant(long durationDays, long durationHours, long durationMinutes) {
        return addSecondsToInstant((((durationDays * 24 + durationHours) * 60) + durationMinutes) * 60);
    }

    public static LocalDate addDayToLocalDate(long days) {
        if (days < 0) {
            return LocalDate.now().minusDays(Math.abs(days));
        } else {
            return LocalDate.now().plusDays(days);
        }
    }

    public static boolean checkTokenExpiration(long expirationTime) {
        return (System.currentTimeMillis() / 1000) >= expirationTime;
    }

    public static String convertToAMPM(String time24) {
        int hour = Integer.parseInt(time24.substring(0, 2));
        int minute = Integer.parseInt(time24.substring(2));

        String period;
        if (hour >= 12) {
            period = "PM";
            if (hour > 12) {
                hour -= 12;
            }
        } else {
            period = "AM";
            if (hour == 0) {
                hour = 12;
            }
        }

        String hourStr = String.format("%02d", hour);
        String minuteStr = String.format("%02d", minute);

        return hourStr + ":" + minuteStr + " " + period;
    }

    public static int getDiffernceOfDate(LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static LocalDateTime add(LocalDateTime time, LocalTime nextTime) {
        if (nextTime == null) {
            return time;
        }
        return time.plusHours(nextTime.getHour()).plusMinutes(nextTime.getMinute());
    }

    public static LocalDateTime minus(LocalDateTime time, LocalTime nextTime) {
        if (nextTime == null) {
            return time;
        }
        return time.minusHours(nextTime.getHour()).minusMinutes(nextTime.getMinute());
    }

    public static LocalDateTime merge(LocalDateTime time, LocalTime nextTime) {
        if (nextTime == null) {
            return time;
        }
        return LocalDateTime.of(time.toLocalDate(), nextTime);
    }

    public static LocalDateTime add(LocalDateTime time, int days) {
        return time.plusDays(days);
    }

    public static LocalDateTime minus(LocalDateTime time, int days) {
        return time.minusDays(days);
    }

    public static LocalTime add(LocalTime time, LocalTime nextTime) {
        if (nextTime == null) {
            return time;
        }
        return time.plusHours(nextTime.getHour()).plusMinutes(nextTime.getMinute());
    }

    public static LocalTime minus(LocalTime time, LocalTime nextTime) {
        if (nextTime == null) {
            return time;
        }
        return time.minusHours(nextTime.getHour()).minusMinutes(nextTime.getMinute());
    }

    public static Duration minus(LocalDateTime time, LocalDateTime nextTime) {
        if (nextTime == null) {
            return null;
        }
        return Duration.between(time, nextTime);
    }

    public static int getDifferenceOfDate(LocalDateTime currentDate, LocalDateTime nextDate) {
        if (nextDate == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(currentDate, nextDate);
    }

    public static int getDifferenceOfHours(LocalDateTime currentDate, LocalDateTime nextDate) {
        if (nextDate == null) {
            return 0;
        }
        return (int) ChronoUnit.HOURS.between(currentDate, nextDate);
    }

    public static boolean isBeforeOrEqual(LocalTime date, LocalTime compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isAfter(date);
    }

    public static boolean inBetween(LocalTime pastTime, LocalTime currentTime, LocalTime futureTime) {

        return isBeforeOrEqual(currentTime, pastTime) && isAfterOrEqual(currentTime, futureTime);
    }

    public static boolean isAfterOrEqual(LocalTime date, LocalTime compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isBefore(date);
    }

    public static boolean isBeforeOrEqual(LocalDateTime date, LocalDateTime compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isAfter(date);
    }

    public static boolean isBeforeOrEqual(LocalDate date, LocalDate compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isAfter(date);
    }

    public static LocalTime getEarlier(LocalTime currentTime, LocalTime otherTime) {
        if (otherTime == null) {
            return currentTime;
        }
        return isAfterOrEqual(currentTime, otherTime) ? currentTime : otherTime;
    }

    public static LocalDateTime getEarlierAndUpdate(LocalDateTime currentTime, LocalDateTime otherDateTime,
            LocalTime otherTime) {
        if (otherTime == null) {
            return currentTime;
        }
        return isAfterOrEqual(currentTime, otherDateTime) ? updateTime(currentTime, otherTime) : otherDateTime;
    }

    public static LocalDateTime updateTime(LocalDateTime currentLocalDateTime, LocalTime otherTime) {
        if (otherTime == null) {
            return currentLocalDateTime;
        }
        return LocalDateTime.of(currentLocalDateTime.toLocalDate(), otherTime);
    }

    public static boolean isAfterOrEqual(LocalDateTime date, LocalDateTime compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isBefore(date);
    }

    public static boolean isAfterOrEqual(LocalDate date, LocalDate compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return !compareToDate.isBefore(date);
    }

    public static LocalTime calculateArrivalTime(LocalTime time, double distance, double speedFactor) {
        double factor = distance / speedFactor * 60;
        return add(time, LocalTime.of((int) (factor / 60), (int) (factor % 60)));
    }

    public static LocalDateTime calculateArrivalTime(LocalDateTime time, double distance, double speedFactor) {
        double factor = distance / speedFactor * 60;
        return add(time, LocalTime.of((int) (factor / 60), (int) (factor % 60)));
    }

    public static LocalTime calculateDepartureTime(LocalTime time, double distance, double speedFactor) {
        double factor = distance / speedFactor * 60;
        return minus(time, LocalTime.of((int) (factor / 60), (int) (factor % 60)));
    }

    public static LocalDateTime calculateDepartureTime(LocalDateTime time, double distance, double speedFactor) {
        double factor = distance / speedFactor * 60;
        return minus(time, LocalTime.of((int) (factor / 60), (int) (factor % 60)));
    }

    public static LocalTime getLocalTime(String[] data, int index) {
        if (index < data.length && data[index] != null && !data[index].trim().isEmpty()) {
            String[] splitTime = data[index].contains(".") ? data[index].split(".") : data[index].split(":");
            int hours = Integer.parseInt(splitTime[0]);
            int minutes = 0;
            if (splitTime.length == 2) {
                minutes = Integer.parseInt(splitTime[1]);
            }
            return LocalTime.of(hours, minutes, 0);
        } else {
            return null;
        }
    }

    public static String getFinancialYear(LocalDate date) {
        if (date == null) {
            return null; // or return "N/A"
        }

        int year = date.getYear();
        int month = date.getMonthValue();

        int startYear = (month <= 3) ? year - 1 : year;
        int endYearShort = (startYear + 1) % 100;

        return String.format("%04d-%02d", startYear, endYearShort);
    }

    /**
     * Returns the start and end dates of the Indian financial year.
     * 
     * @param fyStartYear The starting year of the financial year, e.g., 2023 for FY
     *                    2023-24.
     * @return A pair of LocalDate: start (April 1) and end (March 31 of next year).
     */
    public static FinancialYear getFinancialYearDates(Integer fyStartYear) {
        fyStartYear = Objects.requireNonNullElse(fyStartYear, LocalDate.now().getYear());
        LocalDate startDate = LocalDate.of(fyStartYear, 4, 1);
        LocalDate endDate = LocalDate.of(fyStartYear + 1, 3, 31);
        return new FinancialYear(startDate, endDate);
    }

    @Data
    @AllArgsConstructor
    public static class FinancialYear {
        private final LocalDate startDate;
        private final LocalDate endDate;

    }

}