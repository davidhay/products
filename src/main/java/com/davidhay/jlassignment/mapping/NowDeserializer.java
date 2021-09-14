package com.davidhay.jlassignment.mapping;

import com.davidhay.jlassignment.domain.inbound.Now;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.math.BigDecimal;

public class NowDeserializer extends StdDeserializer<Now> {

  private static final String FROM = "from";
  private static final String TO = "to";

  public NowDeserializer() {
    this(null);
  }

  public NowDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Now deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    if (node == null) {
      return null;
    }
    if (node.isTextual()) {
      return new Now(node.asText());
    } else if (node.isObject()) {
      ObjectNode objNode = (ObjectNode) node;
      Now result = new Now();
      result.setFrom(extractMoney(objNode, FROM));
      result.setTo(extractMoney(objNode, TO));
      return result;
    } else {
      return null;
    }
  }

  private BigDecimal extractMoney(ObjectNode objNode, String textFieldName) {
    Preconditions.checkArgument(objNode != null);
    JsonNode node = objNode.get(textFieldName);
    if (node == null || node instanceof NullNode) {
      return null;
    }
    return JsonUtils.getAmount(node.asText());
  }
}