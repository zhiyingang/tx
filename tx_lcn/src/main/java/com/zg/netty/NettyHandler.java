package com.zg.netty;

import com.alibaba.fastjson.JSONObject;
import com.zg.tx.TransactionType;
import com.zg.tx.TxManager;
import com.zg.tx.TxTransactional;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2018/11/28.
 */
public class NettyHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        String command = "";
        String groupId = "";
        TxTransactional txTransactional = TxManager.getTxTransactional(groupId);
        if(command.equals("rollback")){
            txTransactional.setTransactionType(TransactionType.rollback);
        }else{
            txTransactional.setTransactionType(TransactionType.commit);
        }
        txTransactional.getTask().signalTask();

    }

    public synchronized Object call(JSONObject result){
        context.writeAndFlush(result.toJSONString());
        return null;
    }


}
