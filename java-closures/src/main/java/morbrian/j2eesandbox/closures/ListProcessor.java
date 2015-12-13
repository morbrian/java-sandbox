package morbrian.j2eesandbox.closures;

import java.util.List;
import java.util.function.Function;

public interface ListProcessor<T> {

  public List<T> stageList(List<T> list);

  public List<T> transformList(Function<T,T> tranformation);

}
