package com.davidhay.jlassignment.domain.inbound;

import com.davidhay.jlassignment.util.MoneyUtils;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

  private BigDecimal was;
  private BigDecimal then1;
  private BigDecimal then2;
  private Now now;
  private String currency;

  public boolean hasValidNowPrice() {
    if (now == null) {
      return false;
    }
    return MoneyUtils.isGreaterThanZero(now.getTo());
  }

}
