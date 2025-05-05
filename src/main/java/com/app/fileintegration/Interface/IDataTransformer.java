package com.app.fileintegration.Interface;

import java.util.List;
import java.util.Map;

public interface IDataTransformer {
    List<Map<String, Object>> transform(List<Map<String, Object>> data, List<String> newKesys);
}
