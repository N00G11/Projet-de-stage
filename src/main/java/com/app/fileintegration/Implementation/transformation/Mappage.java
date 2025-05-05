package com.app.fileintegration.Implementation.transformation;

import com.app.fileintegration.Interface.IDataTransformer;
import lombok.Getter;

import java.util.*;

public class Mappage implements IDataTransformer {

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();

    @Override
    public List<Map<String, Object>> transform(List<Map<String, Object>> data, List<String> newKeys) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> originalFields = new ArrayList<>(data.get(0).keySet());

        for (Map<String, Object> originalMap:data){
            Map<String, Object> newMap = new LinkedHashMap<>();

            for (int i = 0; i<originalFields.size();i++){
                String originalfield = originalFields.get(i);
                if (i<newKeys.size()){
                    String newfield = newKeys.get(i);
                    if(!"none".equalsIgnoreCase(newfield)){
                        newMap.put(newfield,originalMap.get(originalfield));
                    }
                }
            }
            result.add(newMap);
        }
        return result;
    }
}
