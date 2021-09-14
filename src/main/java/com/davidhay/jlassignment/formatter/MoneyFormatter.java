package com.davidhay.jlassignment.formatter;

import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.util.MoneyUtils;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class MoneyFormatter {

  private static final String GBP = "GBP";
  private static final Currency DEFAULT = Currency.getInstance(GBP);

  public String formatNowPrice(PriceInfo info) {
    return this.formatNowPrice(info.getNow(), info.getCurrencyCode());
  }

  public String formatNowPrice(BigDecimal bd, String currencyCode) {
    Currency cur = getCurrency(currencyCode);
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    nf.setCurrency(cur);
    if (MoneyUtils.isWholeNumber(bd)) {
      boolean lessThanTen = MoneyUtils.isLessThan10(bd);
      nf.setMaximumFractionDigits(lessThanTen ? 2 : 0);
    } else {
      nf.setMaximumFractionDigits(2);
    }
    return nf.format(bd);
  }


  protected String basicFormatMoney(BigDecimal bd, String currencyCode) {
    Currency cur = getCurrency(currencyCode);
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    nf.setCurrency(cur);
    nf.setMaximumIntegerDigits(bd.scale());
    return nf.format(bd);
  }


  private Currency getCurrency(String code) {
    if (code == null) {
      return DEFAULT;
    }
    try {
      return Currency.getInstance(code);
    } catch (RuntimeException rte) {
      return DEFAULT;
    }
  }
}
