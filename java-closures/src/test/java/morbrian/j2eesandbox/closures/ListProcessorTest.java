package morbrian.j2eesandbox.closures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by hikethru08 on 12/13/15.
 */
public class ListProcessorTest {

  private static final int LIST_SIZE = 1000;
  private Map<String, Long> totals = new HashMap<>();

  public void performTestSequence(TestHarness<?> testHarness, int size) {
    testHarness.generateList(size);
    testHarness.transformList();
  }

  @Before public void primer() {
    List<String> list = new ArrayList();
    for (int i = 0; i < LIST_SIZE; i++) {
      list.add(UUID.randomUUID().toString());
    }
    list.size();
  }

  @Test public void allBenchMarks() {
    benchMarkActivity("linear-string-large", s -> {
      performTestSequence(new StringTestHarness(new LinearListProcessor<>()), LIST_SIZE);
    });
    benchMarkActivity("linear-long-large", s -> {
      performTestSequence(new LongTestHarness(new LinearListProcessor<>()), LIST_SIZE);
    });
    benchMarkActivity("parallel-string-large", s -> {
      performTestSequence(new StringTestHarness(new ParallelListProcessor<>()), LIST_SIZE);
    });
    benchMarkActivity("parallel-long-large", s -> {
      performTestSequence(new LongTestHarness(new ParallelListProcessor<>()), LIST_SIZE);
    });
  }

  private void benchMarkActivity(String tag, Consumer<?> test) {

    long total = 0;
    long runCount = 1;

    for (int i = 0; i < runCount; i++) {
      long startTime = System.currentTimeMillis();
      test.accept(null);
      long stopTime = System.currentTimeMillis();
      long runTime = stopTime - startTime;
      total += runTime;
    }

    System.out.println(
        "[" + tag + "] Tried " + runCount + " runs for Average Run time: " + total / runCount);
  }

}
