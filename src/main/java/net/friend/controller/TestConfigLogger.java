package net.friend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestConfigLogger {

  @GetMapping("/test-log-config")
  public ResponseEntity testNoParam() {

    log.trace(">>> test-log-config TRACE");
    log.debug(">>> test-log-config DEBUG");
    log.info(">>> test-log-config INFO");
    log.warn(">>> test-log-config WARN");
    log.error(">> test-log-config ERROR");

    return new ResponseEntity("Test Log config", HttpStatus.OK);
  }

}
