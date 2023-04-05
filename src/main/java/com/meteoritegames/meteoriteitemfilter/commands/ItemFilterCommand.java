package com.meteoritegames.meteoriteitemfilter.commands;

import com.meteoritegames.meteoriteitemfilter.Main;
import com.meteoritegames.meteoriteitemfilter.objects.Category;
import com.meteoritegames.meteoriteitemfilter.objects.User;
import com.meteoritepvp.api.command.Command;
import com.meteoritepvp.api.command.CommandClass;
import com.meteoritepvp.api.command.DefaultCommand;
import com.meteoritepvp.api.inventory.MeteoriteInventory;
import com.meteoritepvp.api.inventory.presets.BasicInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@DefaultCommand
public class ItemFilterCommand implements CommandClass {
	Main plugin;

	public ItemFilterCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Command(args="add",
			description="Add a material to the item filter.",
			params="@material")
	public void addMaterial(Player player, String[] params) {
		Material material;

		if(params[1].equalsIgnoreCase("hand")){
			material = player.getInventory().getItemInHand().getType();
		}else{
			material = Material.getMaterial(params[1].toUpperCase());
		}

		if(material == null){
			return;
		}else if(material == Material.AIR){
			return;
		}

		User user = plugin.getUser(player);
		user.addMaterial(material);
	}

	@Command(args="remove",
			description="Add a material from the item filter.",
			params="@material")
	public void removeMaterial(Player player, String[] params) {
		Material material;

		if(params[1].equalsIgnoreCase("hand")){
			material = player.getInventory().getItemInHand().getType();
		}else{
			material = Material.getMaterial(params[1].toUpperCase());
		}

		if(material == null){
			return;
		}else if(material == Material.AIR){
			return;
		}

		User user = plugin.getUser(player);
		user.removeMaterial(material);
	}

	@Command(args="reset",
			description="Toggle your item filter on or off")
	public void toggleFilter(Player player) {
		User user = plugin.getUser(player);
		user.setEnabled(!user.isEnabled()); //Reverse toggle
	}

	@Command(args="default",
			description="Reset your item filter")
	public void resetFilter(Player player) {
		User user = plugin.getUser(player);
		user.resetMaterials();
	}

	@Command(args="menu",
			description="Add a material to the item filter.",
			params="@material")
	public void filterMenu(Player player) {
		User user = plugin.getUser(player);
		categoriesGUI(user);
	}

	private void categoriesGUI(User user) {
		int height = plugin.getConfig().getInt("categoriesGUI.size");

		MeteoriteInventory inventory = new MeteoriteInventory(plugin, plugin.getText("categories-title"), 9,height, true);
		BasicInventory page = new BasicInventory(6,height);
		page.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

		for (Category category : plugin.categories) {
			ItemStack categoryItem = buildItem(new ItemStack(category.getMaterial()), category.getName());
			page.setItem(category.getSlot(), categoryItem);
		}

		page.setOnSlotClickListener(e -> {
			for (Category category : plugin.categories) {
				if (e.getSlot() == category.getSlot()) {
					filterGUI(category, user);
					break;
				}
			}
		});

		inventory.setPage(page);
		inventory.show(user.getPlayer());
	}

	private void filterGUI(Category category, User user) {
		int height = category.getSize();

		MeteoriteInventory inventory = new MeteoriteInventory(plugin, category.getTitle(), 9, height, true);
		BasicInventory page = new BasicInventory(6, height);
		page.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

		for (int i = 0; i < category.getItems().size(); i++) {
			Material material = category.getItems().get(i);
			boolean materialEnabled = !user.getMaterials().contains(material);

			ItemStack materialItem;
			if (materialEnabled) materialItem = buildItem(new ItemStack(material), plugin.getText("item-enabled").replace("%item%", new ItemStack(material).getItemMeta().getDisplayName()), Collections.singletonList(plugin.getText("item-enabled-lore")));
			else materialItem = buildItem(new ItemStack(material), plugin.getText("item-disabled").replace("%item%", new ItemStack(material).getItemMeta().getDisplayName()), Collections.singletonList(plugin.getText("item-disabled-lore")));

			page.setItem(i, materialItem);
		}

		page.setOnSlotClickListener(e -> {
			user.toggleMaterial(category.getItems().get(e.getSlot()));
		});

		inventory.setPage(page);
		inventory.show(user.getPlayer());
	}

	private ItemStack buildItem(ItemStack item, String title) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		item.setItemMeta(meta);

		return item;
	}

	private ItemStack buildItem(ItemStack item, String title, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}
}
