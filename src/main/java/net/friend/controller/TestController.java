package net.friend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

  @GetMapping("/test-log")
  public ResponseEntity testNoParam() {

    log.info(" STARTING LOG ...\n... NEW-LINE");

    log.trace(">>> LOG TRACE");
    log.debug(">>> LOG DEBUG");
    log.info(">>> LOG INFO");
    log.warn(">>> LOG WARN");
    log.error(">> LOG ERROR");

    return new ResponseEntity("Test Log", HttpStatus.OK);
  }

}
