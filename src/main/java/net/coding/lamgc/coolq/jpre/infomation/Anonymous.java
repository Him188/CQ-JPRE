package net.coding.lamgc.coolq.jpre.infomation;

import net.coding.lamgc.coolq.jpre.binary.Unpack;

/**
 * 匿名用户
 *
 * @author Him188
 */
public class Anonymous {
	public final long aid;//标识
	public final String nick;//代号
	public final byte[] token;

	public Anonymous(long aid, String nick, byte[] token) {
		this.aid = aid;
		this.nick = nick;
		this.token = token;
	}

	public Anonymous(Unpack unpack) {
		this(unpack.getLong(), unpack.getString(), unpack.getToken());
	}

	public Anonymous(byte[] bytes) {
		this(new Unpack(bytes));
	}

	public byte[] getToken() {
		return token;
	}

	public long getAid() {
		return aid;
	}

	public String getNick() {
		return nick;
	}
}