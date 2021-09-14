package com.davidhay.jlassignment.mapping;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends StdDeserializer<BigDecimal> {

  public BigDecimalDeserializer() {
    this(null);
  }

  public BigDecimalDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return JsonUtils.getAmount(node.asText());
  }
}
