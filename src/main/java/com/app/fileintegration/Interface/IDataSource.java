package com.app.fileintegration.Interface;

import java.util.List;
import java.util.Map;

public interface IDataSource {
    List<Map<String, Object>> extract();
    String getType();
}
