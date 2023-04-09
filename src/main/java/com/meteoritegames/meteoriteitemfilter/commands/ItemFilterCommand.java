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
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@DefaultCommand
public class ItemFilterCommand implements CommandClass {
	private final Main plugin;

	public ItemFilterCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Command(args="add",
			description="Add a material to the item filter.",
			params="@material")
	public void addMaterial(Player player, String[] params) {
		Material material;

		if(params[0].equalsIgnoreCase("hand")) material = player.getInventory().getItemInHand().getType();
		else material = Material.getMaterial(params[0].toUpperCase());

		if(material == null) return;
		if(material == Material.AIR) return;

		player.sendMessage(plugin.getText("material-disabled").replace("%item%", getItemName(material)));
		User user = plugin.getUser(player);
		user.addMaterial(material);
	}

	@Command(args="remove",
			description="Add a material from the item filter.",
			params="@material")
	public void removeMaterial(Player player, String[] params) {
		Material material;

		if(params[0].equalsIgnoreCase("hand")) material = player.getInventory().getItemInHand().getType();
		else material = Material.getMaterial(params[0].toUpperCase());

		if(material == null) return;
		if(material == Material.AIR) return;

		player.sendMessage(plugin.getText("material-enabled").replace("%item%", getItemName(material)));
		User user = plugin.getUser(player);
		user.removeMaterial(material);
	}

	@Command(args="toggle",
			description="Toggle your item filter on or off")
	public void toggleFilter(Player player) {
		User user = plugin.getUser(player);
		if (user.isFilterEnabled()) {
			user.getPlayer().sendMessage(plugin.getText("filter-disabled"));
			user.setFilterEnabled(false);
		} else {
			user.getPlayer().sendMessage(plugin.getText("filter-enabled"));
			user.setFilterEnabled(true);
		}
	}

	@Command(args="reset",
			description="Reset your item filter")
	public void resetFilter(Player player) {
		player.sendMessage(plugin.getText("material-reset"));

		User user = plugin.getUser(player);
		user.resetMaterials();
	}

	@Command(description="Get the filter GUI.")
	public void filter(Player player) {
		User user = plugin.getUser(player);
		categoriesGUI(user);
	}

	@Command(args="menu",
			description="Get the filter GUI.")
	public void filterMenu(Player player) {
		User user = plugin.getUser(player);
		categoriesGUI(user);
	}

	private void categoriesGUI(User user) {
		int height = plugin.getConfig().getInt("categoriesGUI.rows");

		MeteoriteInventory inventory = new MeteoriteInventory(plugin, plugin.getText("categories-title"), 9,height, true);
		BasicInventory page = new BasicInventory(9,height);
		page.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

		for (Category category : plugin.categories) {
			ItemStack categoryItem = buildItem(new ItemStack(category.getMaterial()), category.getName().replaceAll("&", "§"));
			page.setItem(category.getSlot(), categoryItem);
		}

		page.setOnSlotClickListener(e -> {
			if (e.getEvent().getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;

			for (Category category : plugin.categories) {
				if (e.getSlot() == category.getSlot()) {
					user.getPlayer().closeInventory();

					filterGUI(category, user);
					break;
				}
			}
		});

		inventory.applyPage(page);
		inventory.show(user.getPlayer());
	}

	private void filterGUI(Category category, User user) {
		int height = category.getSize();
		System.out.println(height);

		MeteoriteInventory inventory = new MeteoriteInventory(plugin, category.getTitle().replaceAll("&", "§"), 9, height, true);
		BasicInventory page = new BasicInventory(9, height);
		page.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

		for (int i = 0; i < category.getItems().size(); i++) {
			Material material = category.getItems().get(i);
			boolean materialEnabled = !user.getMaterials().contains(material);

			ItemStack materialItem;
			if (materialEnabled) materialItem = buildItem(new ItemStack(material), plugin.getText("item-enabled").replace("%item%",getItemName(material)), Collections.singletonList(plugin.getText("item-enabled-lore")));
			else materialItem = buildItem(new ItemStack(material), plugin.getText("item-disabled").replace("%item%", getItemName(material)), Collections.singletonList(plugin.getText("item-disabled-lore")));

			page.setItem(i, materialItem);
		}

		page.setOnSlotClickListener(e -> {
			if (e.getEvent().getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
			if (category.getItems().size() < e.getSlot()) return;

			Material m = category.getItems().get(e.getSlot());

			if (user.getMaterials().contains(m)) {
				user.removeMaterial(m);
				user.getPlayer().sendMessage(plugin.getText("material-enabled").replace("%item%",getItemName(m)));
				ItemStack materialItem = buildItem(new ItemStack(m), plugin.getText("item-enabled").replace("%item%", getItemName(m)), Collections.singletonList(plugin.getText("item-enabled-lore")));
				page.setItem(e.getSlot(), materialItem);
			} else {
				user.getPlayer().sendMessage(plugin.getText("material-disabled").replace("%item%",getItemName(m)));
				user.addMaterial(m);
				ItemStack materialItem = buildItem(new ItemStack(m), plugin.getText("item-disabled").replace("%item%", getItemName(m)), Collections.singletonList(plugin.getText("item-disabled-lore")));
				page.setItem(e.getSlot(), materialItem);
			}

			inventory.applyPage(page);
			inventory.show(user.getPlayer());
		});

		inventory.applyPage(page);
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

	private String getItemName(Material material) {
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(material));
		return nmsStack.getItem().a(nmsStack);
	}
}
