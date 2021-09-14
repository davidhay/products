package com.davidhay.jlassignment.formatter;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceLabelFormatter {

  private final MoneyFormatter moneyFormatter;

  public PriceLabelFormatter(@Autowired MoneyFormatter moneyFormatter) {
    this.moneyFormatter = moneyFormatter;
  }

  public String getPriceLabel(PriceInfo info, LabelType labelType) {
    Preconditions.checkArgument(info != null);
    switch (labelType) {
      case ShowPercDscount:
        return showPercentageDiscount(info);
      case ShowWasThenNow:
        return showWasThenNow(info);
      default:
      case ShowWasNow:
        return showWasNow(info);
    }
  }

  private String showPercentageDiscount(PriceInfo info) {
    Preconditions.checkArgument(info != null);
    long percDisc = info.getPercentageReduction();
    //If the percDiscount is NOT greater than 1, just show the price now.
    String off = percDisc > 0 ? String.format("%s%% off - ", percDisc) : "";
    String nowPrice = moneyFormatter.formatNowPrice(info.getNow(), info.getCurrencyCode());
    return String.format("%snow %s", off, nowPrice);
  }

  private String showWasNow(PriceInfo info) {
    Preconditions.checkArgument(info != null);
    //We always have 'was' and 'now' - they might be the same if the original data was missing a 'was'
    String currencyCode = info.getCurrencyCode();
    String was = this.moneyFormatter.formatNowPrice(info.getWas(), currencyCode);
    String now = this.moneyFormatter.formatNowPrice(info.getNow(), currencyCode);
    return String.format("Was %s, now %s", was, now);
  }

  private String showWasThenNow(PriceInfo info) {
    Preconditions.checkArgument(info != null);
    //We may not always have a 'then', so fallback to WasNow
    if (info.getThen().isEmpty()) {
      return showWasNow(info);
    }
    String currencyCode = info.getCurrencyCode();
    String was = this.moneyFormatter.formatNowPrice(info.getWas(), currencyCode);
    String then = this.moneyFormatter.formatNowPrice(info.getThen().get(), currencyCode);
    String now = this.moneyFormatter.formatNowPrice(info.getNow(), currencyCode);
    return String.format("Was %s, then %s, now %s", was, then, now);
  }

}
