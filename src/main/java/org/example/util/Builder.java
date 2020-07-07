package org.example.util;

import com.tencentcloudapi.asr.v20190614.models.*;

import java.util.Base64;
import java.util.Map;



public class Builder {
    static StringBuilder  sb = new StringBuilder();

    public static CreateRecTaskRequest buildReq(Map<String, String> prop, byte[] buffer, String useVocab){
        sb.delete(0, sb.length());
        sb.append("{");
        prop.forEach((k,v) -> {
            sb.append("\"" + k + "\":" + v + ",");
        });
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        CreateRecTaskRequest req = CreateRecTaskRequest.fromJsonString(sb.toString(),CreateRecTaskRequest.class);
        req.setDataLen((long) buffer.length);
        if (useVocab != null){
            req.setHotwordId(useVocab);
        }
        String encodeData = Base64.getEncoder().encodeToString(buffer);
        req.setData(encodeData);
        return req;
    }


    public static CreateAsrVocabRequest buildCreateVocabReq(String vocabName, String desc, String vocab, boolean isDefault){

        //sb.delete(0, sb.length());
        //sb.append("{\"Version\":2019-06-14,\"Name\":" + name+"}");
        //CreateAsrVocabRequest req = CreateAsrVocabRequest.fromJsonString(sb.toString(), CreateAsrVocabRequest.class);
        CreateAsrVocabRequest req = new CreateAsrVocabRequest();
        req.setName(vocabName);
        req.setDescription(desc);
        req.setWordWeightStr(vocab);
        req.setWordWeightStr(Base64.getEncoder().encodeToString(vocab.getBytes()));
        return req;

    }

    public static GetAsrVocabListRequest buildGetAsrVocabListReq(){
        GetAsrVocabListRequest req = new GetAsrVocabListRequest();
        return req;
    }

    public static GetAsrVocabRequest buildGetAsrVocabReq(String id) {
        GetAsrVocabRequest req = new GetAsrVocabRequest();
        req.setVocabId(id);
        return req;
    }

    public static UpdateAsrVocabRequest buildUpdateVocabReq(String idText, String nameText, String describeText, String text){
        UpdateAsrVocabRequest req = new UpdateAsrVocabRequest();
        req.setVocabId(idText);
        if (!nameText.equals("")) req.setName(nameText);
        if (!describeText.equals("")) req.setDescription(describeText);
        if (!text.equals("")) {
            req.setWordWeightStr(Base64.getEncoder().encodeToString(text.getBytes()));
        }
        return req;
    }

    public static DeleteAsrVocabRequest buildDeleteVocabReq(String vocabId) {
        DeleteAsrVocabRequest req = new DeleteAsrVocabRequest();
        req.setVocabId(vocabId);
        return req;
    }

    public static SetVocabStateRequest buildSetVocabStateReq(String vocabId, long state) {
        SetVocabStateRequest req = new SetVocabStateRequest();
        req.setVocabId(vocabId);
        req.setState(state);
        return req;
    }

    public static DescribeTaskStatusRequest buildDescribeTaskStatusReq(long taskId) {
        DescribeTaskStatusRequest req = new DescribeTaskStatusRequest();
        req.setTaskId(taskId);
        return req;
    }
}
