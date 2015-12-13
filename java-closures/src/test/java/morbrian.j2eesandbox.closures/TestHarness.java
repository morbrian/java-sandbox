package morbrian.j2eesandbox.closures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hikethru08 on 12/13/15.
 */
public abstract class TestHarness<T> {

  private ListProcessor<T> listProcessor;

  TestHarness(ListProcessor<T> listProcessor) {
    this.listProcessor = listProcessor;
  }

  public void generateList(int count) {
    List<T> list = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      list.add(generateRandomItem());
    }
    listProcessor.stageList(list);
  }

  public void transformList() {
    listProcessor.transformList(item -> { return transformation(item); });
  }

  protected abstract T generateRandomItem();

  public abstract T transformation(T t);
}
