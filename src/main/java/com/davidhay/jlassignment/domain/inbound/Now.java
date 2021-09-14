package com.davidhay.jlassignment.domain.inbound;

import com.davidhay.jlassignment.mapping.JsonUtils;
import com.davidhay.jlassignment.mapping.NowDeserializer;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("DanglingJavadoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * This pojo is used to represent BOTH kinds of the 'product'/'price'/'now' property of the JSON domain response.
 *
 * Kind 1) a json string value containing a monetary amount e.g. "32.00" - this amount is placed into the field 'to'
 *
 * Kind 2) a json object containing both 'from' and 'to' string properties containing monetary amounts
 *    {
 *      "from": "9.00",
 *      "to": "14.00"
 *    }
 *    Both 'from' and 'to' json properties are mapped to the fields of this class.
 * NOTE : amounts value in the inbound response which are empty strings will be mapped to null BigDecimal values.
 * @see NowDeserializer
 */
public class Now {

  private BigDecimal from;
  private BigDecimal to;

  public Now(String now) {
    this.to = JsonUtils.getAmount(now);
    this.from = null;
  }

}
