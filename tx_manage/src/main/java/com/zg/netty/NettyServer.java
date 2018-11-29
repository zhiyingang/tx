package com.zg.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.logging.SocketHandler;

/**
 * Created by Administrator on 2018/11/28.
 */
public class NettyServer {

    public void start(String hostName,int port){
        try{
            final ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup group = new NioEventLoopGroup();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast("handler",new NettyHandler());
                        }
                    });

            bootstrap.bind(hostName,port).sync();
        }catch (InterruptedException e){
            e.fillInStackTrace();
        }
    }

    public void close(){

    }

}
