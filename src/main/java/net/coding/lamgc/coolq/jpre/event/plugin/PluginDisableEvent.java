package net.coding.lamgc.coolq.jpre.event.plugin;

import net.coding.lamgc.coolq.jpre.event.EventTypes;
import net.coding.lamgc.coolq.jpre.event.HandlerList;
import net.coding.lamgc.coolq.jpre.plugin.JavaPlugin;

/**
 * Java 插件停止事件, 非 Java 插件运行器停止
 *
 * @author Him188
 */
public class PluginDisableEvent extends PluginEvent {
	private static final HandlerList handlers = new HandlerList();
	public final JavaPlugin plugin;

	public PluginDisableEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}

	public static int getEventType() {
		return EventTypes.PLUGIN_DISABLE;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
}
