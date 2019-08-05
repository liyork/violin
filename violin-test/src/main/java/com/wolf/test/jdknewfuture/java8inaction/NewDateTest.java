package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/08/03
 */
public class NewDateTest {

    @Test
    public void testDate() {

        //年份的起始选择是1900年，月份的起始从0开始
        Date date = new Date(114, 2, 18);//20140318
        System.out.println("date: " + date);
    }

    @Test
    public void testCreateLocalDate() {
        LocalDate localDate = LocalDate.now();
        System.out.println("now:" + localDate);

        LocalDate of = LocalDate.of(2019, 8, 3);
        System.out.println("of:" + of);

        //可能产生DateTimeParseException
        LocalDate parse = LocalDate.parse("2019-08-03");
        System.out.println("parse:" + parse);
    }

    @Test
    public void testLocalDateGet() {

        LocalDate localDate = LocalDate.now();
        localDate.getYear();
        localDate.getMonth();
        int dayOfMonth = localDate.getDayOfMonth();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int lengthOfMonth = localDate.lengthOfMonth();
        boolean isLeapYear = localDate.isLeapYear();

        System.out.printf("localDate:%s,dayOfMonth:%d,dayOfWeek:%s,lengthOfMonth:%d,isLeapYear:%s",
                localDate, dayOfMonth, dayOfWeek, lengthOfMonth, isLeapYear);
    }

    @Test
    public void testChronoField() {

        LocalDate localDate = LocalDate.now();
        int year = localDate.get(ChronoField.YEAR);
        int month = localDate.get(ChronoField.MONTH_OF_YEAR);
        int day = localDate.get(ChronoField.DAY_OF_MONTH);

        System.out.printf("year:%d,month:%d,day:%s",
                year, month, day);
    }

    @Test
    public void testLocalTimeCreate() {

        LocalTime now = LocalTime.now();
        System.out.println("now:" + now);

        LocalTime of = LocalTime.of(1, 23, 45);
        System.out.println("of:" + of);

        LocalTime parse = LocalTime.parse("01:23:45");
        System.out.println("parse:" + parse);
    }

    @Test
    public void testLocalTimeGet() {

        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();
        System.out.printf("hour:%d,minute:%d,second:%d",
                hour, minute, second);
    }

    @Test
    public void testLocalDateTimeCreate() {

        LocalDateTime now = LocalDateTime.now();
        System.out.println("now:" + now);

        LocalDateTime of1 = LocalDateTime.of(2019, 1, 24, 1, 3, 4);
        System.out.println("of1:" + of1);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime of2 = LocalDateTime.of(localDate, localTime);
        System.out.println("of2:" + of2);
    }

    @Test
    public void testConvert() {

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        //localdate->localdatetime
        LocalDateTime atTime = localDate.atTime(1, 2, 3);
        System.out.println("atTime：" + atTime);

        //localdate->localdatetime
        LocalDateTime atTime2 = localDate.atTime(localTime);
        System.out.println("atTime2：" + atTime2);

        //localtime->localdatetime
        LocalDateTime atDate = localTime.atDate(localDate);
        System.out.println("atDate：" + atDate);
    }

    @Test
    public void testLocalDateTimeGet() {

        LocalDateTime localDateTime = LocalDateTime.now();

        LocalDate toLocalDate = localDateTime.toLocalDate();
        System.out.println("toLocalDate:" + toLocalDate);
        LocalTime toLocalTime = localDateTime.toLocalTime();
        System.out.println("toLocalTime:" + toLocalTime);
    }

    //计算机的建模时间最自然的格式是表示一个持续时间段上某个点的单一大整型数。
    //以Unix元年时间（传统的设定为UTC时区1970年1月1日午夜时分）开始所经历的秒数进行计算
    //Instant的设计初衷是为了便于机器使用。
    @Test
    public void testInstant() {

        Instant now = Instant.now();
        System.out.println("now:" + now);
        System.out.println(System.currentTimeMillis());
        System.out.println(now.toEpochMilli());

        Instant instant = Instant.ofEpochSecond(3);
        Instant instant1 = Instant.ofEpochSecond(3, 0);
        //用1_000_000_000 ns 调整
        Instant instant2 = Instant.ofEpochSecond(2, 1_000_000_000);
        Instant instant3 = Instant.ofEpochSecond(4, -1_000_000_000);
        System.out.println("instant:" + instant + ",is same:" +
                (instant.compareTo(instant1) == 0 && instant1.compareTo(instant2) == 0 && instant2.compareTo(instant3) == 0));
    }

    //以秒和纳秒衡量时间的长短
    @Test
    public void testDurationCreate() {

//        Duration.between(LocalDate.now(), LocalDate.now());//报错，只能以秒和纳秒衡量时间的长短
        Duration.between(LocalTime.now(), LocalTime.now());
        Duration.between(LocalDateTime.now(), LocalDateTime.now());
        Duration.between(Instant.now(), Instant.now());
//        Duration.between(LocalTime.now(), Instant.now());//报错，不能与Instant混用

        Duration.ofDays(1);
        Duration.ofMinutes(3);
        Duration.of(3, ChronoUnit.MINUTES);
        Duration.ofSeconds(3);

        //以年、月或者日的方式计算
        Period.between(LocalDate.now(), LocalDate.now());
        Period.ofWeeks(3);
        Period.ofDays(4);
        Period.of(1, 2, 3);
    }

    //以年、月或者日的方式衡量时间的长短
    @Test
    public void testPeriodCreate() {

        Period.between(LocalDate.now(), LocalDate.now());
        Period.ofWeeks(3);
        Period.ofDays(4);
        Period.of(1, 2, 3);
    }

    @Test
    public void testUpdateNew() {

        LocalDate now = LocalDate.now();
        //绝对
        LocalDate localDate1 = now.withYear(2018);
//        System.out.println(localDate1);
        LocalDate localDate2 = localDate1.withDayOfMonth(6);
        System.out.println(localDate2);
        LocalDate localDate3 = localDate2.with(ChronoField.MONTH_OF_YEAR, 9);
        System.out.println(localDate3);

        //相对
        LocalDate localDate4 = localDate3.plusWeeks(2);
        LocalDate localDate5 = localDate3.minusYears(3);
        System.out.println(localDate5);

        now.plus(7, ChronoUnit.MONTHS);
    }

    @Test
    public void testAdjust() {

        LocalDate now = LocalDate.now();
        LocalDate with = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        System.out.println(now + "_" + with);

        LocalDate with1 = now.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(with1);
    }

    @Test
    public void testMyAdjust() {

        LocalDate now = LocalDate.now();
        now.with(new NextWorkingDay());

        //建议封装成类，便于理解以及复用
        now.with(getNextWorkingDay());
    }

    //方式1
    class NextWorkingDay implements TemporalAdjuster {

        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek day = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

            int addDay;
            if (day == DayOfWeek.FRIDAY) {
                addDay = 3;
            } else if (day == DayOfWeek.SATURDAY) {
                addDay = 2;
            } else {
                addDay = 1;
            }
            return temporal.plus(addDay, ChronoUnit.DAYS);
        }
    }

    //方式2
    private TemporalAdjuster getNextWorkingDay() {
        return TemporalAdjusters.ofDateAdjuster(temporal -> {
            DayOfWeek day = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

            int addDay;
            if (day == DayOfWeek.FRIDAY) {
                addDay = 3;
            } else if (day == DayOfWeek.SATURDAY) {
                addDay = 2;
            } else {
                addDay = 1;
            }
            return temporal.plus(addDay, ChronoUnit.DAYS);
        });
    }

    //DateTimeFormatter实例都是线程安全
    @Test
    public void testFormat() {

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        System.out.println(LocalDate.parse("20190804", DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(LocalDate.parse("2019-08-04", DateTimeFormatter.ISO_LOCAL_DATE));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatStr = localDate.format(formatter);
        System.out.println("formatStr:" + formatStr);
        LocalDate parse = LocalDate.parse(formatStr, formatter);
        System.out.println("parse:" + parse);

        //创建某个Locale的格式器
        DateTimeFormatter italianFormatter =
                DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        String formattedDate = LocalDate.now().format(italianFormatter);
        System.out.println("formattedDate:" + formattedDate);
        LocalDate date2 = LocalDate.parse(formattedDate, italianFormatter);
        System.out.println(date2);

        DateTimeFormatter iformatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
        LocalDate parse1 = LocalDate.parse("4. agosto 2019", iformatter);
        System.out.println(parse1);
    }

    //地区ID都为“{区域}/{城市}”的格式，这些地区集合的设定都由英特网编号分配机构（ IANA）的时区数据库提供
    @Test
    public void testZoneCreate() {

        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneRules rules = romeZone.getRules();
        System.out.println(rules);

        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        System.out.println(shanghaiZone.getRules());

        //旧到新
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        System.out.println(zoneId);
    }

    //2019-08-04 T19:54:41.039 +08:00[Asia/Shanghai]
    //localDate  localtime     zone
    //localdatetime            zone
    //zonedatetime
    @Test
    public void testAddZoneForTime() {

        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");

        LocalDate localDate = LocalDate.now();
        ZonedDateTime zonedDateTime1 = localDate.atStartOfDay(romeZone);
        System.out.println(zonedDateTime1);
        ZonedDateTime zonedDateTime2 = localDate.atStartOfDay(shanghaiZone);
        System.out.println(zonedDateTime2);

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(shanghaiZone);
        System.out.println(zonedDateTime);

        Instant instant = Instant.now();
        ZonedDateTime zonedDateTime3 = instant.atZone(shanghaiZone);
        System.out.println(zonedDateTime3);
    }

    @Test
    public void testZoneConvert() {

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        System.out.println(instant);

        ZoneId romeZone = ZoneId.of("Europe/Rome");
        LocalDateTime localDateTime1 = localDateTime.ofInstant(instant, romeZone);
        System.out.println(localDateTime1);
    }

    @Test
    public void testZoneOffset() {

        //利用当前时区和UTC/格林尼治的固定偏差。纽约落后于伦敦5小时。表示的是当前时间和伦敦格林尼治子午线时间的差异
        //“-05:00”的偏差实际上对应的是美国东部标准时间
        //使用这种方式定义的ZoneOffset并未考虑任何日光时的影响，所以在大多数情况下，不推荐使用
        ZoneOffset of = ZoneOffset.of("-05:00");

        //使用ISO-8601的历法系统，以相对于UTC/格林尼治时间的偏差方式表示日期时间。
//        LocalDateTime dateTime = LocalDateTime.now();
//        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(date, newYorkOffset);
    }
}
