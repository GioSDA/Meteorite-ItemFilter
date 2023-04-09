package com.meteoritegames.meteoriteitemfilter.objects;

import org.bukkit.Material;

import java.util.List;

public class Category {
	private final int size;
	private final Material material;
	private final String name;
	private final int slot;
	private final String title;
	private final List<Material> items;

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

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public int getSlot() {
		return slot;
	}

	public String getTitle() {
		return title;
	}

	public List<Material> getItems() {
		return items;
	}
}
