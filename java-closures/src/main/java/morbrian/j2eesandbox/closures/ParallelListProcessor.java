package morbrian.j2eesandbox.closures;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by hikethru08 on 12/13/15.
 */
public class ParallelListProcessor<T> implements ListProcessor<T> {

  private List<T> inputList;

  @Override public List<T> stageList(List<T> list) {
    return this.inputList = Collections.unmodifiableList(list);
  }

  @Override public List<T> transformList(Function<T, T> transformation) {
    if (transformation == null) {
      return inputList.parallelStream().map(item -> {
        return item;
      }).collect(Collectors.toList());
    } else {
      return inputList.parallelStream().map(transformation).collect(Collectors.toList());
    }
  }

}
