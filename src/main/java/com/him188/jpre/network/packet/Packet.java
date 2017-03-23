package com.him188.jpre.network.packet;

import com.him188.jpre.binary.Unpack;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author Him188
 */
abstract public class Packet {
	abstract public byte[] encode();

	abstract public void decode(Unpack unpack);

	abstract public byte getNetworkId();

	/**
	 * 所有包都必须有的常量
	 * 若该包是由客户端发送的包, 该包必须有一个无参数的构造器(否则构造失败并抛出异常), 该常量用于服务器接收到数据包后识别并构造实例(否则接受失败, 无事件)
	 * 若该包是发送给客户端的包, 该常量用于酷Q识别.
	 */
	public static final byte NETWORK_ID;

	public ChannelHandlerContext ctx;

	public final void setChannelHandlerContext(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public final ChannelHandlerContext getChannelHandlerContext() {
		return ctx;
	}


	public static byte getNetworkId(Packet packet) throws NoSuchFieldException, IllegalAccessException {
		Field field = packet.getClass().getField("NETWORK_ID");
		return (byte) field.get(null);
	}

	static {
		NETWORK_ID = -1;

		PACKETS = new Class<?>[32];
		PACKET_IDS = new byte[32];
		PACKETS_COUNT = 0;

		try {
			registerPacket(ClientPingPacket.class);
			registerPacket(ServerPongPacket.class);
			registerPacket(CommandResultPacket.class);
			registerPacket(DisablePluginPacket.class);
			registerPacket(DisablePluginResultPacket.class);
			registerPacket(EnablePluginPacket.class);
			registerPacket(EnablePluginResultPacket.class);
			registerPacket(EventResultPacket.class);
			registerPacket(GetPluginInformationPacket.class);
			registerPacket(GetPluginInformationResultPacket.class);
			registerPacket(InvalidEventPacket.class);
			registerPacket(InvalidIdPacket.class);
			registerPacket(LoadPluginDescriptionPacket.class);
			registerPacket(LoadPluginDescriptionResultPacket.class);
			registerPacket(LoadPluginPacket.class);
			registerPacket(LoadPluginResultPacket.class);
			registerPacket(LoginPacket.class);
			registerPacket(LoginResultPacket.class);
			registerPacket(LogPacket.class);
			registerPacket(SetInformationPacket.class);
			registerPacket(SetInformationResultPacket.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final Class<?>[] PACKETS;
	private static final byte[] PACKET_IDS;
	private static int PACKETS_COUNT;

	public static void registerPacket(Class<? extends Packet> clazz) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
		if (clazz == Packet.class){
			throw new IllegalArgumentException("class cannot be Packet.class");
		}

		PACKETS[PACKETS_COUNT] = clazz;
		PACKET_IDS[PACKETS_COUNT++] = (byte) clazz.getField("NETWORK_ID").get(null);
	}

	public static Class<?>[] getPackets() {
		return PACKETS;
	}

	public static byte[] getPacketIds() {
		return PACKET_IDS;
	}

	public static int getPacketsCount() {
		return PACKETS_COUNT;
	}

	public static Packet matchPacket(byte id) {
		for (int i = 0; i < PACKET_IDS.length; i++) {
			if (PACKET_IDS[i] == id) {
				try {
					Constructor<?> constructor = PACKETS[i].getConstructor();
					constructor.setAccessible(true);
					return (Packet) constructor.newInstance();
				} catch (Throwable e) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(Id:" + this.getNetworkId() + ")";
	}
}
