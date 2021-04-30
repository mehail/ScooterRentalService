package com.senla.srs.dto.promocod;

import com.senla.srs.dto.AbstractDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class PromoCodDTO extends AbstractDTO {
    @NonNull
    @Length(min = 1, max = 64, message = "Name must be between 1 and 64 characters")
    private String name;
    @NonNull
    private LocalDate startDate;
    private LocalDate expiredDate;
    @NotNull
    @Min(value = 0, message = "Discount percentage must be positive")
    private Integer discountPercentage;
    @NotNull
    @Min(value = 0, message = "Bonus point percentage must be positive")
    private Integer bonusPoint;
    @NonNull
    private Boolean available;
}
