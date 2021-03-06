package net.mamoe.jpre.event.group;

import net.mamoe.jpre.Group;
import net.mamoe.jpre.QQ;
import net.mamoe.jpre.RobotQQ;
import net.mamoe.jpre.event.HandlerList;

/**
 * 群聊消息. 一些消息如颜文字的特殊符号收到时会被转义为 unicode.
 * Called when receive group message
 *
 * @author Him188 @ JPRE Project
 */
public class GroupMessageEvent extends GroupEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }


    private final String message;

    public GroupMessageEvent(RobotQQ robot, Group group, QQ qq, String message) {
        super(robot, group, qq);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
