package org.example.service;


import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.example.util.Builder;
import org.example.util.Property;

public class YuYin {

    public static final YuYin INSTANCE = new YuYin();


    private final AsrClient client;

    private YuYin() {
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


    public CreateRecTaskResponse requestServ(byte[] buffer, String useVocab) {
        try {
            return client.CreateRecTask(Builder.buildReq(Property.propMap, buffer, useVocab));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getResult(long taskId) throws TencentCloudSDKException {
        DescribeTaskStatusRequest req = Builder.buildDescribeTaskStatusReq(taskId);

        DescribeTaskStatusResponse resp;

        while ((resp = client.DescribeTaskStatus(req)).getData().getStatus() != 2) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resp.getData().getResult();
    }

    public CreateAsrVocabResponse createHotWords(String vocabName, String vocabDesc, String vocab, boolean isDefault) throws TencentCloudSDKException {
        CreateAsrVocabResponse resp = client.CreateAsrVocab(Builder.buildCreateVocabReq(vocabName, vocabDesc, vocab, isDefault));
        setVocabState(resp.getVocabId(), isDefault ? 1 : 0);
        return resp;
    }

    public Vocab[] listHotWords() {
        try {
            GetAsrVocabListResponse resp = client.GetAsrVocabList(Builder.buildGetAsrVocabListReq());
            return resp.getVocabList();
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vocab getVocabById(String id) {
        try {
            GetAsrVocabResponse resp = client.GetAsrVocab(Builder.buildGetAsrVocabReq(id));
            return Vocab.fromJsonString(GetAsrVocabResponse.toJsonString(resp), Vocab.class);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }


    public UpdateAsrVocabResponse updateVocabs(String idText, String nameText, String describeText, String text, boolean isDefault) throws TencentCloudSDKException {
        if (idText.equals("")) return null;
        UpdateAsrVocabResponse resp = client.UpdateAsrVocab(Builder.buildUpdateVocabReq(idText, nameText, describeText, text));
        setVocabState(resp.getVocabId(), isDefault ? 1 : 0);
        return resp;
    }

    public DeleteAsrVocabResponse deleteVocabs(String vocabId) throws TencentCloudSDKException {
        if (vocabId.equals("")) return null;
        return client.DeleteAsrVocab(Builder.buildDeleteVocabReq(vocabId));
    }

    public SetVocabStateResponse setVocabState(String vocabId, long state) {
        try {
            return client.SetVocabState(Builder.buildSetVocabStateReq(vocabId, state));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }
}

