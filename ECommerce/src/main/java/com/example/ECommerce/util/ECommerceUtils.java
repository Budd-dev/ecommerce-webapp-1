package com.example.ECommerce.util;

import com.example.ECommerce.exception.BadRequestException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ECommerceUtils {
    public static final BigDecimal ZERO_BIG_DECIMAL = BigDecimal.ZERO;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal initializeBigDecimal() {
        return ZERO_BIG_DECIMAL;
    }

    public static void validateId(Integer id, String object) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid " + object + " ID");
        }
    }
}
