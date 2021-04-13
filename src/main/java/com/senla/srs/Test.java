package com.senla.srs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime begin = LocalDateTime.parse("2021-03-01 00:00", formatter);
        System.out.println("begin = " + begin);

        LocalDateTime end = LocalDateTime.parse("2021-03-02 00:00", formatter);
        System.out.println("end = " + end);

        LocalDate dateBegin = begin.toLocalDate();
        LocalTime timeBegin = begin.toLocalTime();

        LocalDate dateEnd = end.toLocalDate();
        LocalTime timeEnd = end.toLocalTime();



    }


}
