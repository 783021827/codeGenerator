package com.myGenerator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {

    public static String coverObject2Json(Object obj){
        if(obj==null){
            return null;
        }
       return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }
}
