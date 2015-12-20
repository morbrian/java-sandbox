package morbrian.j2eesandbox.jpabasics.poller;

import org.slf4j.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import morbrian.j2eesandbox.jpabasics.digest.DigestController;

@Singleton @Startup public class DataRequestScheduler {

  @Inject private Logger log;

  @Inject private DigestController controller;

  @Schedule(second = "0", minute = "*/2", hour = "*") public void pollForData() {
    log.info("Poller Fetching Data");
    controller.requestDataFromClient();
  }

}
