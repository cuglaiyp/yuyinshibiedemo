package org.example;


import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import java.util.Base64;

public class YuYinUtil {

    public static final YuYinUtil INSTANCE = new YuYinUtil();

    private AsrClient client;

    private YuYinUtil() {
        Credential cred = new Credential(Property.propMap.get("id"), Property.propMap.get("password"));
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(Property.propMap.get("endpoint"));
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        client = new AsrClient(cred, Property.propMap.get("region"), clientProfile);
        Property.propMap.remove("id");
        Property.propMap.remove("password");
        Property.propMap.remove("endpoint");
        Property.propMap.remove("region");
    }


    public CreateRecTaskResponse requestServ(byte[] buffer) {
        try {
            Builder builder = new Builder(Property.propMap);
            CreateRecTaskRequest req = builder.buildReq();
            req.setDataLen((long) buffer.length);
            String encodeData = Base64.getEncoder().encodeToString(buffer);
            req.setData(encodeData);
            CreateRecTaskResponse resp = client.CreateRecTask(req);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getResult(long taskId) {
        try {
            String params = "{\"TaskId\":" + taskId + "}";
            DescribeTaskStatusRequest req = DescribeTaskStatusRequest.fromJsonString(params, DescribeTaskStatusRequest.class);

            DescribeTaskStatusResponse resp = null;

            while ((resp = client.DescribeTaskStatus(req)).getData().getStatus() != 2){
                Thread.sleep(1000);
            }
            return resp.getData().getResult();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }



}

