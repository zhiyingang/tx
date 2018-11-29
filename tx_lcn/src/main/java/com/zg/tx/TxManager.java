package com.zg.tx;

import com.alibaba.fastjson.JSONObject;
import com.zg.netty.NettyClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/11/28.
 */
public class TxManager {

    private static NettyClient nettyClient;

    private static Map<String,TxTransactional> LB_MAP = new HashMap();

    private static ThreadLocal<TxTransactional> threadLocal = new ThreadLocal();

    public static void setNettyClient(NettyClient nettyClient) {
        TxManager.nettyClient = nettyClient;
    }

    private static ThreadLocal<String> groupLocal = new ThreadLocal();

    public static String createGroup(){
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("command","create");
        nettyClient.send(jsonObject);
        System.out.println("create tcl");
        return groupId;
    }

    public static TxTransactional createTransation(String groupId){
        String transationId = UUID.randomUUID().toString();
        TxTransactional txTransactional = new TxTransactional(groupId,transationId);
        System.out.println("create transation");
        LB_MAP.put(groupId,txTransactional);
        threadLocal.set(txTransactional);
        return txTransactional;
    }

    public static TxTransactional submitTransation(TxTransactional txTransactional,Boolean isEnd,TransactionType transactionType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",txTransactional.getGroupId());
        jsonObject.put("transactionId",txTransactional.getTransactionId());
        jsonObject.put("tranType",transactionType);
        jsonObject.put("command","add");
        jsonObject.put("isEnd",isEnd);
        nettyClient.send(jsonObject);
        System.out.println("submit tcl");
        return txTransactional;
    }


    public static TxTransactional getTxTransactional(String groupId){
        return LB_MAP.get(groupId);
    }

    public static TxTransactional getTxTransactional(){
        return threadLocal.get();
    }

    public static void setGroupLocal(String groupId){
        groupLocal.set(groupId);
    }

    public static String getGroupLocal(){
        return groupLocal.get();
    }



}
