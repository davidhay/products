package com.davidhay.jlassignment.domain.outbound;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.util.MoneyUtils;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class PriceInfoTest {

  final BigDecimal _198 = MoneyUtils.getMoney("198");
  final BigDecimal _90 = MoneyUtils.getMoney("90");
  final BigDecimal _99 = MoneyUtils.getMoney("99");
  final BigDecimal _110 = MoneyUtils.getMoney("110.00");
  final BigDecimal _110_01 = MoneyUtils.getMoney("110.01");

  //PriceInfo - same was and now => no reduction
  @Test
  void testNoWasMeansNoReduction() {
    PriceInfo info = getPriceInfo(BigDecimal.ONE, BigDecimal.ONE);
    assertFalse(info.isReduction());
    assertThat(info.getReductionAmtPence()).isZero();
    assertThat(info.getPercentageReduction()).isZero();
    assertThat(info.getWas()).isEqualTo(info.getNow());
    assertThat(info.getThen()).isEmpty();
  }

  @Test
  void test10percentReduction() {
    PriceInfo info = getPriceInfo(_110, _99);
    assertTrue(info.isReduction());
    assertThat(info.getReductionAmtPence()).isEqualTo(1100);
    assertThat(info.getPercentageReduction()).isEqualTo(10);
    assertThat(info.getWas()).isEqualByComparingTo(_110);
    assertThat(info.getNow()).isEqualByComparingTo(_99);
    assertThat(info.getThen()).isEmpty();
  }

  @Test
  void test100percentReduction() {
    PriceInfo info = getPriceInfo(_110, ZERO);
    assertTrue(info.isReduction());
    assertThat(info.getReductionAmtPence()).isEqualTo(11000);
    assertThat(info.getPercentageReduction()).isEqualTo(100);
    assertThat(info.getWas()).isEqualByComparingTo(_110);
    assertThat(info.getNow()).isEqualByComparingTo(ZERO);
    assertThat(info.getThen()).isEmpty();
  }

  @Test
  void test10percentIncrease() {
    PriceInfo info = getPriceInfo(_90, _99);
    assertFalse(info.isReduction());
    assertThat(info.getReductionAmtPence()).isEqualTo(-900);
    assertThat(info.getPercentageReduction()).isEqualTo(-10);
    assertThat(info.getWas()).isEqualByComparingTo(_90);
    assertThat(info.getNow()).isEqualByComparingTo(_99);
    assertThat(info.getThen()).isEmpty();
  }

  @Test
  void test100percentIncrease() {
    PriceInfo info = getPriceInfo(_99, _198);
    assertFalse(info.isReduction());
    assertThat(info.getPriceReductionAmountPence()).isEqualTo(-9900L);
    assertThat(info.getPercentageReduction()).isEqualTo(-100);
    assertThat(info.getWas()).isEqualByComparingTo(_99);
    assertThat(info.getNow()).isEqualByComparingTo(_198);
    assertThat(info.getThen()).isEmpty();
  }


  private PriceInfo getPriceInfo(BigDecimal was, BigDecimal now) {
    return new PriceInfo("GBP", was, now);
  }

  @Test
  void testPriceReduction() {
    {
      PriceInfo pinfo1 = getPriceInfo(_110_01, _110_01);
      assertFalse(pinfo1.isReduction());
      assertThat(pinfo1.getPriceReductionAmountPence()).isEqualTo(0);
      assertThat(pinfo1.getPercentageReduction()).isEqualTo(0);
    }
    {
      PriceInfo pinfo2 = getPriceInfo(_110, _110_01);
      assertFalse(pinfo2.isReduction());
      assertThat(pinfo2.getPriceReductionAmountPence()).isEqualTo(-1);
      assertThat(pinfo2.getPercentageReduction()).isEqualTo(0);
    }
    {
      PriceInfo pinfo3 = getPriceInfo(_110_01, _110);
      assertTrue(pinfo3.isReduction());
      assertThat(pinfo3.getPriceReductionAmountPence()).isEqualTo(1);
      assertThat(pinfo3.getPercentageReduction()).isEqualTo(0);
    }
  }


}
