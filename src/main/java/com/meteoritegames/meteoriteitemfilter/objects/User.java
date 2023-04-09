package com.meteoritegames.meteoriteitemfilter.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
	private final UUID uuid;
	private Set<Material> materials;
	private boolean filterEnabled;

	public User(final UUID uuid, Set<Material> materials) {
		this.uuid = uuid;
		this.filterEnabled = false;
		this.materials = materials;
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

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public Set<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<Material> materials) {
		this.materials = materials;
	}

	public boolean isFilterEnabled() {
		return filterEnabled;
	}

	public void setFilterEnabled(boolean filterEnabled) {
		this.filterEnabled = filterEnabled;
	}
}
