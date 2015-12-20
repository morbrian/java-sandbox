package morbrian.sandbox.j2ee.rdbms2hive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NamesAndRecords implements Serializable {

  private List<String> fieldNames;

  private List<List<Object>> records;

  transient private Map<String, Integer> nameToIndex;

  public NamesAndRecords(List<String> fieldNames, List<List<Object>> records) {
    this.fieldNames = Collections.unmodifiableList(fieldNames);
    List<List<Object>> container = new ArrayList<>();
    for (List<Object> list : records) {
      container.add(Collections.unmodifiableList(list));
    }
    this.records = Collections.unmodifiableList(container);
  }

  private synchronized Map<String, Integer> getIndexMap() {
    if (nameToIndex == null) {
      Map<String, Integer> tmpMap = new HashMap<String, Integer>();
      // map indices to help quickly identify field values later
      for (int i = 0; i < records.size(); i++) {
        tmpMap.put(fieldNames.get(i), i);
      }
      nameToIndex = tmpMap;
    }
    return nameToIndex;
  }

  public List<String> getFieldNames() {
    return fieldNames;
  }

  public List<List<Object>> getRecords() {
    return records;
  }

  public int getIndexForFieldName(String fieldName) {
    return getIndexMap().get(fieldName);
  }
}
