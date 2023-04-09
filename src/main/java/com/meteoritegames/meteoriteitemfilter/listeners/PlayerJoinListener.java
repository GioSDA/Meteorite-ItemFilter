package com.meteoritegames.meteoriteitemfilter.listeners;

import com.meteoritegames.meteoriteitemfilter.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	private final Main plugin;

	public PlayerJoinListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (plugin.getUser(e.getPlayer()) == null) plugin.addUser(e.getPlayer());
	}

}
