package morbrian.j2eesandbox.closures;

/**
 * Created by hikethru08 on 12/13/15.
 */
public class LongTestHarness extends TestHarness<Long> {

  LongTestHarness(ListProcessor<Long> listProcessor) {
    super(listProcessor);
  }

  @Override protected Long generateRandomItem() {
    return Math.round(Math.random() * Integer.MAX_VALUE);
  }

  @Override public Long transformation(Long aLong) {
    return Math.floorMod(aLong, 7);
  }

}
