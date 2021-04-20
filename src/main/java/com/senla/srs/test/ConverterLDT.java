package com.senla.srs.test;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//@Converter(autoApply = true)
public class ConverterLDT implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        Timestamp date1 = localDateTime == null ? null : Timestamp.valueOf(localDateTime);
        System.out.println("\n\n\n\n\n\n\n convertToDatabaseColumn " + date1 +  "\n\n\n\n\n\n\n\n");
        System.out.println("date1 = " + date1.toLocalDateTime());
        return date1;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        System.out.println("\n\n\n\n\n\n\n convertToEntityAttribute \n\n\n\n\n\n\n\n");
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
