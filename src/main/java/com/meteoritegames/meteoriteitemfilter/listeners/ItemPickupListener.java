package com.meteoritegames.meteoriteitemfilter.listeners;

import com.meteoritegames.meteoriteitemfilter.Main;
import com.meteoritegames.meteoriteitemfilter.objects.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPickupListener implements Listener {
	Main plugin;

	public ItemPickupListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();

		Material material = e.getItem().getItemStack().getType();
		ItemStack item = e.getItem().getItemStack();

		User user = plugin.getUser(p);
		if (user == null) return;

		e.setCancelled(true);

		if (!user.getMaterials().contains(material)) e.setCancelled(false);
		if(plugin.getConfig().getBoolean("ignore_custom_items", true)
				&& item.getItemMeta() != null
				&& (item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore() || item.getItemMeta().hasEnchants())
		)	e.setCancelled(false);
	}
}
