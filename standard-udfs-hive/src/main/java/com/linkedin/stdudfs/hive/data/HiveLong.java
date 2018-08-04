package com.linkedin.stdudfs.hive.data;

import com.linkedin.stdudfs.api.StdFactory;
import com.linkedin.stdudfs.api.data.StdLong;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.LongObjectInspector;


public class HiveLong extends HiveData implements StdLong {

  final LongObjectInspector _longObjectInspector;

  public HiveLong(Object object, LongObjectInspector longObjectInspector, StdFactory stdFactory) {
    super(stdFactory);
    _object = object;
    _longObjectInspector = longObjectInspector;
  }

  @Override
  public long get() {
    return _longObjectInspector.get(_object);
  }

  @Override
  public ObjectInspector getUnderlyingObjectInspector() {
    return _longObjectInspector;
  }
}