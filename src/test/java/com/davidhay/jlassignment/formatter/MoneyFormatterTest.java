package com.davidhay.jlassignment.formatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.mapping.JsonUtils;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MoneyFormatterTest {

  final MoneyFormatter formatter = new MoneyFormatter();

  @Nested
  class BasicFormatMoney {

    @Test
    public void testPounds() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("£12.34", formatter.basicFormatMoney(amt, "GBP"));
    }

    @Test
    public void testEuro() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("€12.34", formatter.basicFormatMoney(amt, "EUR"));
    }

    @Test
    public void testDollar() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("US$12.34", formatter.basicFormatMoney(amt, "USD"));
    }

    @Test
    public void testJapaneseYen() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("JP¥12.34", formatter.basicFormatMoney(amt, "JPY"));
    }

    @Test
    public void testNullCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("£12.34", formatter.basicFormatMoney(amt, null));
    }

    @Test
    public void testEmptyCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("£12.34", formatter.basicFormatMoney(amt, ""));
    }

    @Test
    public void testUnknownCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assert amt != null;
      assertEquals("£12.34", formatter.basicFormatMoney(amt, "bob"));
    }
  }

  @Nested
  class NowPriceFormatting {

    @Test
    public void test9WholePounds() {
      BigDecimal amt = JsonUtils.getAmount("9");
      assertEquals("£9.00", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void test9Pounds10p() {
      BigDecimal amt = JsonUtils.getAmount("9.1");
      assertEquals("£9.10", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void test15Pounds() {
      BigDecimal amt = JsonUtils.getAmount("15");
      assertEquals("£15", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void test15Pounds10p() {
      BigDecimal amt = JsonUtils.getAmount("15.1");
      assertEquals("£15.10", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void test10WholePounds() {
      BigDecimal amt = JsonUtils.getAmount("10");
      assertEquals("£10", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void test10Pounds10p() {
      BigDecimal amt = JsonUtils.getAmount("10.1");
      assertEquals("£10.10", formatter.formatNowPrice(amt, "GBP"));
    }

    @Test
    public void testEuro() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("€12.34", formatter.formatNowPrice(amt, "EUR"));
    }

    @Test
    public void testDollar() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("US$12.34", formatter.formatNowPrice(amt, "USD"));
    }

    @Test
    public void testJapaneseYen() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("JP¥12.34", formatter.formatNowPrice(amt, "JPY"));
    }

    @Test
    public void testNullCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("£12.34", formatter.formatNowPrice(amt, null));
    }

    @Test
    public void testEmptyCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("£12.34", formatter.formatNowPrice(amt, ""));
    }

    @Test
    public void testUnknownCode() {
      BigDecimal amt = JsonUtils.getAmount("12.34");
      assertEquals("£12.34", formatter.formatNowPrice(amt, "bob"));
    }

  }

  @Nested
  class NowPriceFormattingForPriceInfo {

    MoneyFormatter sut;

    @Captor
    ArgumentCaptor<BigDecimal> argAmt;

    @Captor
    ArgumentCaptor<String> argCC;

    @BeforeEach
    void setup() {
      sut = Mockito.spy(new MoneyFormatter());
    }

    @Test
    void testDelegateForPriceInfo() {
      String nowPrice = UUID.randomUUID().toString();
      doReturn(nowPrice).when(sut).formatNowPrice(argAmt.capture(), argCC.capture());

      String randomCC = UUID.randomUUID().toString();
      BigDecimal randomAmt = BigDecimal.valueOf(Math.random());
      PriceInfo info = new PriceInfo(randomCC, randomAmt);

      String result = sut.formatNowPrice(info);
      assertThat(result).isEqualTo(nowPrice);

      assertThat(argAmt.getValue()).isEqualTo(randomAmt);
      assertThat(argCC.getValue()).isEqualTo(randomCC);

      verify(sut).formatNowPrice(info);
      verify(sut).formatNowPrice(argAmt.getValue(), argCC.getValue());

      verifyNoMoreInteractions(sut);
    }
  }
}