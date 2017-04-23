package com.him188.jpre.network;

import com.him188.jpre.Frame;
import com.him188.jpre.JPREMain;
import com.him188.jpre.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 网络数据包接收器. 该类属于网络层, 插件一般不需要使用
 *
 * @author Him188
 */
public class NetworkPacketHandler extends SimpleChannelInboundHandler<byte[]> {
	private JPREMain jpre;

	public JPREMain getJPREMain() {
		return jpre;
	}

	NetworkPacketHandler(JPREMain jpre){
		this.jpre = jpre;
	}


	private static List<MPQClient> clients = new ArrayList<>();

	public static List<MPQClient> getClients() {
		return clients;
	}


	private ConcurrentLinkedQueue<byte[]> dataTemp = new ConcurrentLinkedQueue<>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {
		synchronized (this){
			System.out.println("[Network] Data packet received: " + Arrays.toString(data));

			if (data.length >= 2) {
				if (data[data.length - 2] == (byte) 127 && data[data.length - 1] == (byte) 127) {
					dataTemp.add(Utils.arrayDelete(data, 2));
					byte[] realData = new byte[0];
					while ((data = dataTemp.poll()) != null) {
						realData = Utils.arrayAppend(realData, data);
					}
					final byte[] finalRealData = realData;
					for (MPQClient client : clients) {
						if (client.is(ctx.channel().remoteAddress())) {
							client.getFrame().getScheduler().scheduleTask(null, () -> client.dataReceive(finalRealData));
						}
					}
					return;
				}
			}

			dataTemp.add(data);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) { //远程主机强迫关闭了一个现有的连接
			return;
		}

		super.exceptionCaught(ctx, cause);

		// TODO: 2017/3/22  配置是否显示错误信息.
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (!clients.isEmpty()) { //只允许一个MPQ连接
			return;
		}

		MPQClient client = new MPQClient(new Frame(getJPREMain()), ctx.channel().remoteAddress(), ctx);
		clients.add(client);

		System.out.println("[Network] RemoteClient: " + ctx.channel().remoteAddress() + " connected.");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		MPQClient found = null;
		for (MPQClient client : clients) {
			if (found != null) {
				continue;
			}
			if (client.is(ctx.channel().remoteAddress())) {
				found = client;
			}
		}
		if (found != null) {
			clients.remove(found);
		}
		System.out.println("[Network] RemoteClient: " + ctx.channel().remoteAddress() + " disconnected.");
		super.channelInactive(ctx);
	}
}