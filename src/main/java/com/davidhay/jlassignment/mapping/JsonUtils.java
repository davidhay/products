package com.davidhay.jlassignment.mapping;

import com.davidhay.jlassignment.util.MoneyUtils;
import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;

/**
 * We will represent Money as BigDecimals but when the data comes from an external 'productCatalog'
 * source - we perform error handling.
 */
@Log4j2
public class JsonUtils {

  public static BigDecimal getAmount(String data) {
    try {
      if (data == null || data.isBlank()) {
        return null;
      }
      return MoneyUtils.getMoney(data);
    } catch (RuntimeException ex) {
      log.error(String.format("problem converting %s to BigDecimal", data), ex);
      return null;
    }
  }

}
