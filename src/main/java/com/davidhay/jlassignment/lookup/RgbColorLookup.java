package com.davidhay.jlassignment.lookup;

import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RgbColorLookup {

  private final Properties props = new Properties();

  public RgbColorLookup() {
    ResourceBundle raw = ResourceBundle.getBundle("colorToRgb");
    for (Iterator<String> keys = raw.getKeys().asIterator(); keys.hasNext(); ) {
      String key = keys.next();
      props.put(normalize(key), raw.getString(key));
    }
    log.info("Just loaded [{}] color2rgb mappings!", props.size());
  }

  public Optional<String> getRgbColor(String basicColor) {
    if (basicColor == null) {
      return Optional.empty();
    }
    String hex = props.getProperty(normalize(basicColor));
    return Optional.ofNullable(hex);
  }

  String normalize(String value) {
    if (value == null) {
      return null;
    }
    return value.replaceAll("\\s", "").toUpperCase();
  }

}
