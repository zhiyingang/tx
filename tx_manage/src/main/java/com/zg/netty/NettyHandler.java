package com.zg.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/28.
 */
public class NettyHandler extends ChannelInboundHandlerAdapter {

    private static List<Channel> group = new ArrayList<Channel>();


    private static Map<String,List<String>> typeMap = new HashMap<String, List<String>>();

    private static Map<String,Integer> countMap = new HashMap<String, Integer>();

    private static Map<String,Boolean> isEndMap = new HashMap<String, Boolean>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        group.add(ctx.channel());

    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String command = "";
        String groupId = "";
        String tranType = "";
        Integer count = 1;
        Boolean isEnd = null;

        if("create".equals(command)){
            typeMap.put(groupId,new ArrayList<String>());
        }else if("add".equals(command)){
            typeMap.get(groupId).add(tranType);
            if(isEnd){
                isEndMap.put(groupId,true);
                countMap.put(groupId, count);//全部发消息  状态
            }

            JSONObject result = new JSONObject();
            result.put("groupId",groupId);
            if(isEndMap.get(groupId) && countMap.get(groupId).equals(typeMap.get(groupId).size())){
                if(typeMap.get(groupId).contains("rollback")){
                    result.put("command","rollback");
                    sendReuldt(result);
                }else{
                    result.put("command","commit");
                    sendReuldt(result);
                }
            }

        }

    }

    private void sendReuldt(JSONObject result){
        for(Channel channel:group){
            channel.writeAndFlush(result.toJSONString());
        }
    }

    @Override
    protected void ensureNotSharable() {
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        return super.isSharable();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
