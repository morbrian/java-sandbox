package morbrian.j2eesandbox.closures;

import java.util.UUID;

/**
 * Created by hikethru08 on 12/13/15.
 */
public class StringTestHarness extends TestHarness<String> {

  StringTestHarness(ListProcessor listProcessor) {
    super(listProcessor);
  }

  @Override protected String generateRandomItem() {
    return UUID.randomUUID().toString();
  }

  @Override public String transformation(String s) {
    return s + "++" + UUID.randomUUID().toString();
  }

}
