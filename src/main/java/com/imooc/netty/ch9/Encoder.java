package com.imooc.netty.ch9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * ---------------------
 *|   4    |  4  |  ?   |
 * ---------------------
 *| length | age | name |
 * ---------------------
 */


/**
 * 编码器
 */
public class Encoder extends MessageToByteEncoder<User> {
    /**
     *
     * 解码步骤 基类ByteToEncoderMessage
     * 1 读到的字节流累加到 ByteBuf
     * 2 子类的decide进行解析
     * 3 解析到的byteBuf向下传播
     * @param ctx
     * @param user
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, User user, ByteBuf out) throws Exception {

        byte[] bytes = user.getName().getBytes();
        out.writeInt(4 + bytes.length);
        out.writeInt(user.getAge());
        out.writeBytes(bytes);
    }
}
