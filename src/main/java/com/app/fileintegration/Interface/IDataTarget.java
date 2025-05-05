package com.app.fileintegration.Interface;

import java.util.List;
import java.util.Map;

public interface IDataTarget {
    void load(List<Map<String, Object>> data);
}
