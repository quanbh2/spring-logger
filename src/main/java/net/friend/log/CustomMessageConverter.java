package net.friend.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CustomMessageConverter extends MessageConverter {

  public String convert(ILoggingEvent event) {
    return enhance(super.convert(event));
  }

  private String enhance(String incoming) {
    return incoming.replaceAll("\n", "[NEW_LINE]");
  }

}
