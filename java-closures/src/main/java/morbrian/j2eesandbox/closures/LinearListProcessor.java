package morbrian.j2eesandbox.closures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by hikethru08 on 12/13/15.
 */
public class LinearListProcessor<T> implements ListProcessor<T> {

  private List<T> inputList;

  @Override public List<T> stageList(List<T> list) {
    return this.inputList = Collections.unmodifiableList(list);
  }

  @Override public List<T> transformList(Function<T, T> transformation) {
    List<T> outputList = new ArrayList<>(inputList.size());
    if (transformation == null) {
      inputList.forEach(item -> {
        outputList.add(item);
      });
    } else {
      inputList.forEach(item -> {
        outputList.add(transformation.apply(item));
      });
    }
    return outputList;
  }

}
