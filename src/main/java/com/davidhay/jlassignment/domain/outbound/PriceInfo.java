package com.davidhay.jlassignment.domain.outbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/**
 * The Price pojo that we derive from raw pricing data. This pojo always has 'was' and 'now' - keeps
 * things simpler. This class has immutable fields - all fields are derived at construction time
 * When the 'was' is equal to 'now' - it's just like there was no price change. Going to store the
 * price change as a reduction - a price increase would be negative reduction.
 */
@Getter
public class PriceInfo {

  private static final BigDecimal _100 = new BigDecimal(100);

  private final String currencyCode;
  private final BigDecimal was;
  private final BigDecimal now;
  private final Optional<BigDecimal> then;

  //an increase is stored as negative reduction
  @JsonIgnore
  private final long reductionAmtPence;

  //an increase is stored as a negative percentage
  @JsonIgnore
  private final long percentageReduction;

  public PriceInfo(
      BigDecimal now) {
    this("GBP", now);
  }

  public PriceInfo(
      String currencyCode,
      BigDecimal now) {
    this(currencyCode, now, Optional.empty(), now);
  }

  public PriceInfo(String currencyCode, BigDecimal was,
      BigDecimal now) {
    this(currencyCode, was, Optional.empty(), now);
  }

  public PriceInfo(String currencyCode, @NonNull BigDecimal was,
      @NonNull Optional<BigDecimal> then,
      @NonNull BigDecimal now
  ) {

    this.currencyCode = currencyCode;
    this.was = was;
    this.now = now;
    this.then = then;

    BigDecimal reduction = was.subtract(now);

    reductionAmtPence = reduction.multiply(_100).longValue();

    percentageReduction = reduction.divide(was, RoundingMode.HALF_UP).multiply(_100)
        .setScale(0, RoundingMode.HALF_UP).longValue();
  }

  public boolean isReduction() {
    return this.reductionAmtPence > 0;
  }

  public Long getPriceReductionAmountPence() {
    return reductionAmtPence;
  }

}
