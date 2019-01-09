package com.yangbingdong.springboot.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lzt
 * 根据指定时间生成cron表达式
 **/
@Slf4j
public class CronUtils {
	private static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(CRON_FORMAT);

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @return
     */
    public static String formatDateByPattern(Date date) {
		SimpleDateFormat sdf = getSimpleDateFormat();
		Objects.requireNonNull(date);
		return  sdf.format(date);
    }

	private static SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat(CRON_FORMAT);
	}

	public static String getCron(Date date) {
        return formatDateByPattern(date);
    }

	public static String getCron(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return FORMATTER.format(localDateTime);
	}

    /***
     * convert cron to date, eg "0 07 10 15 1 ? 2016"
     * @param dateCron  : 时间点
     * @return
     */
    public static Date getDateByCron(String dateCron) {
		SimpleDateFormat sdf = getSimpleDateFormat();
        Date date = null;
        try {
            date = sdf.parse(dateCron);
        }catch (Exception e){
            log.error("转换cron时间失败,源时间为" + dateCron);
        }
        return date;
    }
}
