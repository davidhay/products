package com.davidhay.jlassignment.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.davidhay.jlassignment.util.MoneyUtils;
import org.junit.jupiter.api.Test;

public class JsonUtilsTest {

  @Test
  public void testNullAmount() {
    assertNull(JsonUtils.getAmount(null));
    assertNull(JsonUtils.getAmount(""));
    assertNull(JsonUtils.getAmount("  "));
    assertNull(JsonUtils.getAmount("bob"));
  }

  @Test
  public void testValidAmount() {
    assertThat(MoneyUtils.getMoney("0")).isEqualByComparingTo(JsonUtils.getAmount("0"));
    assertThat(MoneyUtils.getMoney("1")).isEqualByComparingTo(JsonUtils.getAmount("1.0"));
    assertThat(MoneyUtils.getMoney("1.2")).isEqualByComparingTo(JsonUtils.getAmount("1.2"));
    assertThat(MoneyUtils.getMoney("1.23")).isEqualByComparingTo(JsonUtils.getAmount("1.23"));
    assertThat(MoneyUtils.getMoney("1.234")).isEqualByComparingTo(JsonUtils.getAmount("1.234"));
    assertThat(MoneyUtils.getMoney("1.24")).isEqualByComparingTo(JsonUtils.getAmount("1.237"));
  }
}
