package com.meteoritegames.meteoriteitemfilter.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
	private final Player player;
	private Set<Material> materials;
	private boolean isEnabled;

	public User(final Player player, Set<Material> materials) {
		this.player = player;
		this.isEnabled = false;
		this.materials = materials;
	}

	public void toggleMaterial(Material material) {
		if (materials.contains(material)) {
			removeMaterial(material);
		} else {
			addMaterial(material);
		}
	}

	public void addMaterial(Material material) {
		materials.add(material);
	}

	public void removeMaterial(Material material) {
		materials.remove(material);
	}

	public void resetMaterials() {
		materials = new HashSet<>();
	}

	public String materialsToString() {
		StringBuilder builder = new StringBuilder();
		for (Material material : materials) {
			builder.append(material.name()).append(",");
		}
		return builder.toString();
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
