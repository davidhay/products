package com.davidhay.jlassignment.util;

import static com.davidhay.jlassignment.util.MoneyUtils.getMoney;
import static com.davidhay.jlassignment.util.MoneyUtils.isGreaterThanZero;
import static com.davidhay.jlassignment.util.MoneyUtils.isLessThan10;
import static com.davidhay.jlassignment.util.MoneyUtils.isNotSame;
import static com.davidhay.jlassignment.util.MoneyUtils.isWholeNumber;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

public class MoneytUtilsTest {

  @Test
  public void testGetMoney() {
    assertThat(getMoney("1.2345")).isEqualByComparingTo(
        new BigDecimal("1.23").setScale(2, RoundingMode.HALF_UP));
    assertThat(getMoney("1")).isEqualByComparingTo(
        new BigDecimal("1.00").setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  public void testLessThan10() {
    assertThat(isLessThan10(getMoney("9.99"))).isTrue();
    assertThat(isLessThan10(getMoney("10"))).isFalse();
    assertThat(isLessThan10(getMoney("10.01"))).isFalse();
    assertThat(isLessThan10(getMoney("-9.99"))).isTrue();
    assertThat(isLessThan10(getMoney("-10"))).isTrue();
  }

  @Test
  public void testGreaterThanZero() {
    assertThat(isGreaterThanZero(getMoney("-1"))).isFalse();
    assertThat(isGreaterThanZero(getMoney("0"))).isFalse();
    assertThat(isGreaterThanZero(getMoney("1"))).isTrue();
  }

  @Test
  public void testIsNotSame() {
    BigDecimal ten = getMoney("10.00");
    BigDecimal twenty = getMoney("20");

    assertThat(isNotSame(TEN, ten)).isFalse();
    assertThat(isNotSame(twenty, ten)).isTrue();
    assertThat(isNotSame(TEN, twenty.subtract(ten))).isFalse();
  }


  @Test
  public void testIsWholNumber() {
    assertThat(isWholeNumber(ZERO)).isTrue();
    assertThat(isWholeNumber(ONE)).isTrue();
    assertThat(isWholeNumber(ONE.negate())).isTrue();
    assertThat(isWholeNumber(TEN)).isTrue();

    assertThat(isWholeNumber(getMoney("12.00"))).isTrue();
    assertThat(isWholeNumber(getMoney("12.01"))).isFalse();
    assertThat(isWholeNumber(getMoney("12.99"))).isFalse();
  }
}
