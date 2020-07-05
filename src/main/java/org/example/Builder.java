package org.example;

import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;

import java.util.Map;



public class Builder {
    StringBuilder sb;
    Map<String, String> prop;
    Builder(Map<String, String> prop){
        sb = new StringBuilder();
        this.prop = prop;
    }

    public CreateRecTaskRequest buildReq(){
        sb.append("{");
        prop.forEach((k,v) -> {
            sb.append("\"" + k + "\":" + v + ",");
        });
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        CreateRecTaskRequest req = CreateRecTaskRequest.fromJsonString(sb.toString(),CreateRecTaskRequest.class);
        return req;
    }


}
