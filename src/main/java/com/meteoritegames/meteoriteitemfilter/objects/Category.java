package com.meteoritegames.meteoriteitemfilter.objects;

import org.bukkit.Material;

import java.util.List;

public class Category {
	int size;
	Material material;
	String name;
	int slot;
	String title;
	List<Material> items;

	public Category(int size, Material material, String name, int slot, String title, List<Material> items) {
		this.size = size;
		this.material = material;
		this.name = name;
		this.slot = slot;
		this.title = title;
		this.items = items;
	}


	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public List<Material> getItems() {
		return items;
	}

	public void setItems(List<Material> items) {
		this.items = items;
	}
}
