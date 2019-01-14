package com.honcz.zrpc.zrpcserialization.coder;

import com.honcz.zrpc.zrpcserialization.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * @author hongbin
 * Created on 21/10/2017
 */
@AllArgsConstructor
public class RPCEncoder extends MessageToByteEncoder {

	private Class<?> genericClass;
	private Serializer serializer;

	@Override
	public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if (genericClass.isInstance(in)) {
			byte[] data = serializer.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
}
