package com.meteoritegames.meteoriteitemfilter.objects;

import com.meteoritegames.meteoriteitemfilter.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class User {
	Main plugin;
	private final Player player;
	private Set<Material> materials;
	private boolean isEnabled;

	public User(Main plugin, final Player player, Set<Material> materials) {
		this.plugin = plugin;
		this.player = player;
		this.isEnabled = false;
		this.materials = materials;
	}

	public void toggleMaterial(Material material) {
		if (materials.contains(material)) {
			player.sendMessage(plugin.getText("material-enabled").replace("%item%", material.name()));
			removeMaterial(material);
		} else {
			player.sendMessage(plugin.getText("material-disabled").replace("%item%", material.name()));
			addMaterial(material);
		}
	}

	public void addMaterial(Material material) {
		player.sendMessage(plugin.getText("material-disabled").replace("%item%", material.name()));
		materials.add(material);
	}

	public void removeMaterial(Material material) {
		player.sendMessage(plugin.getText("material-enabled").replace("%item%", material.name()));
		materials.remove(material);
	}

	public void resetMaterials() {
		player.sendMessage(plugin.getText("materials-reset"));
		materials = new HashSet<>();
	}

	public Player getPlayer() {
		return player;
	}

	public Set<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<Material> materials) {
		this.materials = materials;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
