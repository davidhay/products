package com.davidhay.jlassignment.mapper;

import com.davidhay.jlassignment.domain.inbound.Price;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.util.MoneyUtils;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/*
 Derives PriceInfo from Price instances which have valid 'now'
*/
@Component
public class PriceToPriceInfoMapper {

  public PriceInfo getPriceInfo(Price price) {
    Preconditions.checkArgument(price != null);
    Preconditions.checkArgument(price.hasValidNowPrice());
    return getPriceInfoForValidPrice(price);
  }

  private PriceInfo getPriceInfoForValidPrice(Price price) {
    BigDecimal now = price.getNow().getTo();

    BigDecimal rawWas = price.getWas();

    //if we don't have a 'was' value - assume it's the same as 'now' - no price change - prevent errors, simpler code.
    BigDecimal was = rawWas != null ? rawWas : now;

    //we will use the first of 'then2' and 'then1' that is 'non null' and is not the same as 'was'
    Optional<BigDecimal> optThen = Stream.of(price.getThen2(), price.getThen1())
        .filter(Objects::nonNull)
        .filter(amt -> MoneyUtils.isNotSame(amt, was))
        .findFirst();

    return new PriceInfo(price.getCurrency(), was, optThen, now);
  }

}
