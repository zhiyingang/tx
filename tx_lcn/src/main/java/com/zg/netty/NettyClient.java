package com.zg.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * Created by Administrator on 2018/11/28.
 */
@Component
public class NettyClient implements InitializingBean {

    private NettyHandler nettyHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        start("",8888);
    }

    public void start(String hostName,int port){
        nettyHandler = new NettyHandler();
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("decoder",new StringDecoder());
                        pipeline.addLast("encoder",new StringEncoder());
                        pipeline.addLast("handler",nettyHandler);
                    }
                });
        try {
            b.connect(hostName,port).sync();
        } catch (InterruptedException e){
            e.fillInStackTrace();
        }
    }

    public void send(JSONObject jsonObject){
        nettyHandler.call(jsonObject);
    }
}
