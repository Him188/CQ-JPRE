package com.him188.jpre.event.group;

import com.him188.jpre.Frame;
import com.him188.jpre.event.Event;

abstract public class GroupEvent extends Event {
	abstract public long getGroup();

	abstract public Frame getFrame();
}
