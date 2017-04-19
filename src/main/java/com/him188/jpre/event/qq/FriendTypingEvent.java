package com.him188.jpre.event.qq;

import com.him188.jpre.QQ;
import com.him188.jpre.RobotQQ;

/**
 * 好友"正在输入"
 * "正在输入": 使用电脑 QQ, 对方正在打字时聊天窗口顶部对方名字旁边会显示 "正在输入".
 *
 * @author Him188
 */
public class FriendTypingEvent extends QQEvent {
	public FriendTypingEvent(RobotQQ robot, QQ qq) {
		super(robot, qq);
	}
}