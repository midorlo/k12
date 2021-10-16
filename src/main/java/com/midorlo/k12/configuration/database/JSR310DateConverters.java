package com.midorlo.k12.configuration.database;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.time.*;
import java.util.Date;

/**
 * <p>JSR310DateConverters class.</p>
 */
public final class JSR310DateConverters {

    private JSR310DateConverters() {}

    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

        private LocalDateToDateConverter() {}

        @Override
        public Date convert(@NonNull LocalDate source) {
            return Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {

        public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();

        private DateToLocalDateConverter() {}

        @Override
        public LocalDate convert(@NonNull Date source) {
            return ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }
    }

    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

        private ZonedDateTimeToDateConverter() {}

        @Override
        public Date convert(@NonNull ZonedDateTime source) {
            return Date.from(source.toInstant());
        }
    }

    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

        private DateToZonedDateTimeConverter() {}

        @Override
        public ZonedDateTime convert(@NonNull Date source) {
            return ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

        public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();

        private LocalDateTimeToDateConverter() {}

        @Override
        public Date convert(@NonNull LocalDateTime source) {
            return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();

        private DateToLocalDateTimeConverter() {}

        @Override
        public LocalDateTime convert(@NonNull Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public static class DurationToLongConverter implements Converter<Duration, Long> {

        public static final DurationToLongConverter INSTANCE = new DurationToLongConverter();

        private DurationToLongConverter() {}

        @Override
        public Long convert(@NonNull Duration source) {
            return source.toNanos();
        }
    }

    public static class LongToDurationConverter implements Converter<Long, Duration> {

        public static final LongToDurationConverter INSTANCE = new LongToDurationConverter();

        private LongToDurationConverter() {}

        @Override
        public Duration convert(@NonNull Long source) {
            return Duration.ofNanos(source);
        }
    }
}
