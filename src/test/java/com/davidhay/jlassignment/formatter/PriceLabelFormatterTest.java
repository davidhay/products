package com.davidhay.jlassignment.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.mapping.JsonUtils;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PriceLabelFormatterTest {

  private final PriceLabelFormatter sut = new PriceLabelFormatter((new MoneyFormatter()));

  final PriceInfo pi10_20_USD = new PriceInfo("USD", amount("10"), Optional.empty(), amount("20"));
  final PriceInfo pi10_20_EUR = new PriceInfo("EUR", amount("10"), Optional.empty(), amount("20"));
  final PriceInfo pi10_20_GBP = new PriceInfo("GBP", amount("10"), Optional.empty(), amount("20"));

  final PriceInfo pi20_10_USD = new PriceInfo("USD", amount("20"), Optional.empty(), amount("10"));
  final PriceInfo pi20_10_EUR = new PriceInfo("EUR", amount("20"), Optional.empty(), amount("10"));
  final PriceInfo pi20_10_GBP = new PriceInfo("GBP", amount("20"), Optional.empty(), amount("10"));

  final PriceInfo pi20_666_USD = new PriceInfo("USD", amount("20"), Optional.empty(),
      amount("6.66"));
  final PriceInfo pi20_666_EUR = new PriceInfo("EUR", amount("20"), Optional.empty(),
      amount("6.66"));
  final PriceInfo pi20_666_GBP = new PriceInfo("GBP", amount("20"), Optional.empty(),
      amount("6.66"));

  final PriceInfo pi777_666_USD = new PriceInfo("USD", amount("7.77"), Optional.empty(),
      amount("6.66"));
  final PriceInfo pi777_666_EUR = new PriceInfo("EUR", amount("7.77"), Optional.empty(),
      amount("6.66"));
  final PriceInfo pi777_666_GBP = new PriceInfo("GBP", amount("7.77"), Optional.empty(),
      amount("6.66"));

  final PriceInfo pi20_20_USD = new PriceInfo("USD", amount("20"), Optional.empty(), amount("20"));
  final PriceInfo pi20_20_EUR = new PriceInfo("EUR", amount("20"), Optional.empty(), amount("20"));
  final PriceInfo pi20_20_GBP = new PriceInfo("GBP", amount("20"), Optional.empty(), amount("20"));

  final PriceInfo pi20_15_10_USD = new PriceInfo("USD", amount("20"), Optional.of(amount("15")),
      amount("10"));
  final PriceInfo pi20_15_10_EUR = new PriceInfo("EUR", amount("20"), Optional.of(amount("15")),
      amount("10"));
  final PriceInfo pi20_15_10_GBP = new PriceInfo("GBP", amount("20"), Optional.of(amount("15")),
      amount("10"));

  final PriceInfo pi999_666_333_GBP = new PriceInfo("GBP", amount("9.99"),
      Optional.of(amount("6.66")), amount("3.33"));
  final PriceInfo pi999_666_3_GBP = new PriceInfo("GBP", amount("9.99"),
      Optional.of(amount("6.66")), amount("3"));
  final PriceInfo pi999_6_3_GBP = new PriceInfo("GBP", amount("9.99"), Optional.of(amount("6")),
      amount("3"));
  final PriceInfo pi9_6_3_GBP = new PriceInfo("GBP", amount("9"), Optional.of(amount("6")),
      amount("3"));

  final PriceInfo pi1999_666_333_GBP = new PriceInfo("GBP", amount("19.99"),
      Optional.of(amount("6.66")), amount("3.33"));
  final PriceInfo pi1999_666_3_GBP = new PriceInfo("GBP", amount("19.99"),
      Optional.of(amount("6.66")), amount("3"));
  final PriceInfo pi1999_6_3_GBP = new PriceInfo("GBP", amount("19.99"), Optional.of(amount("6")),
      amount("3"));
  final PriceInfo pi19_6_3_GBP = new PriceInfo("GBP", amount("19"), Optional.of(amount("6")),
      amount("3"));

  private BigDecimal amount(String value) {
    return JsonUtils.getAmount(value);
  }

  private void checkLabel(String expected, LabelType type, PriceInfo priceInfo) {
    String actual = sut.getPriceLabel(priceInfo, type);
    assertEquals(expected, actual);
  }

  @Nested
  class ShowWasNow {

    private final LabelType type = LabelType.ShowWasNow;

    @Test
    void testReductionNotBelow10() {
      checkLabel("Was £20, now £10", type, pi20_10_GBP);
      checkLabel("Was €20, now €10", type, pi20_10_EUR);
      checkLabel("Was US$20, now US$10", type, pi20_10_USD);
    }

    @Test
    void testReductionBelow10() {
      checkLabel("Was £20, now £6.66", type, pi20_666_GBP);
      checkLabel("Was €20, now €6.66", type, pi20_666_EUR);
      checkLabel("Was US$20, now US$6.66", type, pi20_666_USD);

      checkLabel("Was £7.77, now £6.66", type, pi777_666_GBP);
      checkLabel("Was €7.77, now €6.66", type, pi777_666_EUR);
      checkLabel("Was US$7.77, now US$6.66", type, pi777_666_USD);
    }

    @Test
    void testWasNowSame() {
      checkLabel("Was £20, now £20", type, pi20_20_GBP);
      checkLabel("Was €20, now €20", type, pi20_20_EUR);
      checkLabel("Was US$20, now US$20", type, pi20_20_USD);
    }

    @Test
    void testIncrease() {
      checkLabel("Was £10, now £20", type, pi10_20_GBP);
      checkLabel("Was €10, now €20", type, pi10_20_EUR);
      checkLabel("Was US$10, now US$20", type, pi10_20_USD);
    }
  }


  @Nested
  class ShowWasThenNow {

    private final LabelType type = LabelType.ShowWasThenNow;

    @Test
    void testReduction() {
      checkLabel("Was £20, then £15, now £10", type, pi20_15_10_GBP);
      checkLabel("Was €20, then €15, now €10", type, pi20_15_10_EUR);
      checkLabel("Was US$20, then US$15, now US$10", type, pi20_15_10_USD);
    }

    @Test
    void testNoThen() {
      checkLabel("Was £20, now £10", type, pi20_10_GBP);
      checkLabel("Was €20, now €10", type, pi20_10_EUR);
      checkLabel("Was US$20, now US$10", type, pi20_10_USD);
    }

    @Test
    void testDecimalPlacesAllBelow10() {
      checkLabel("Was £9.99, then £6.66, now £3.33", type, pi999_666_333_GBP);
      checkLabel("Was £9.99, then £6.66, now £3.00", type, pi999_666_3_GBP);
      checkLabel("Was £9.99, then £6.00, now £3.00", type, pi999_6_3_GBP);
      checkLabel("Was £9.00, then £6.00, now £3.00", type, pi9_6_3_GBP);
    }

    @Test
    void testDecimalPlacesSomeBelow10() {
      checkLabel("Was £19.99, then £6.66, now £3.33", type, pi1999_666_333_GBP);
      checkLabel("Was £19.99, then £6.66, now £3.00", type, pi1999_666_3_GBP);
      checkLabel("Was £19.99, then £6.00, now £3.00", type, pi1999_6_3_GBP);
      checkLabel("Was £19, then £6.00, now £3.00", type, pi19_6_3_GBP);
    }
  }


  @Nested
  class ShowPercDscount {

    private final LabelType type = LabelType.ShowPercDscount;

    @Test
    void testMoneyOff() {
      checkLabel("50% off - now £10", type, pi20_15_10_GBP);
      checkLabel("50% off - now €10", type, pi20_15_10_EUR);
      checkLabel("50% off - now US$10", type, pi20_15_10_USD);
    }

    @Test
    void testNoChange() {
      checkLabel("now £20", type, pi20_20_GBP);
      checkLabel("now €20", type, pi20_20_EUR);
      checkLabel("now US$20", type, pi20_20_USD);
    }

    @Test
    void testPriceIncrease() {
      checkLabel("now £20", type, pi10_20_GBP);
      checkLabel("now €20", type, pi10_20_EUR);
      checkLabel("now US$20", type, pi10_20_USD);
    }
  }
}
