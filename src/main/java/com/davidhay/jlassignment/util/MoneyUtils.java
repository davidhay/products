package com.davidhay.jlassignment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * We are going to represent monetary amounts as BigDecimals 1) we can create them using Strings 2)
 * we can do calculations, scaling and rounding
 * <p>
 * Have added utility methods - because comparing BigDecimals can be error prone.
 */
public class MoneyUtils {

  public static BigDecimal getMoney(String amt) {
    return new BigDecimal(amt).setScale(2, RoundingMode.HALF_UP);
  }

  public static boolean isLessThan10(BigDecimal amt) {
    return amt.compareTo(BigDecimal.TEN) < 0;
  }

  public static boolean isGreaterThanZero(BigDecimal amt) {
    return amt != null && BigDecimal.ZERO.compareTo(amt) < 0;
  }

  public static boolean isNotSame(BigDecimal amt1, BigDecimal amt2) {
    return amt1.compareTo(amt2) != 0;
  }

  public static boolean isWholeNumber(BigDecimal amt) {
    if (amt == null) {
      return false;
    }
    return amt.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
  }
}
