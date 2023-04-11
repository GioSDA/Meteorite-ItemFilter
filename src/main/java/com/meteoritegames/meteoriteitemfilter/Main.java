package com.meteoritegames.meteoriteitemfilter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meteoritegames.meteoriteitemfilter.commands.ItemFilterCommand;
import com.meteoritegames.meteoriteitemfilter.listeners.ItemPickupListener;
import com.meteoritegames.meteoriteitemfilter.listeners.PlayerJoinListener;
import com.meteoritegames.meteoriteitemfilter.objects.Category;
import com.meteoritegames.meteoriteitemfilter.objects.User;
import com.meteoritepvp.api.MeteoritePlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main extends MeteoritePlugin {
	private static Set<User> users = new HashSet<>();
	public HashMap<String, String> text = new HashMap<>();
	public Set<Category> categories = new HashSet<>();

	@Override
	protected void onInit() {
		super.onInit();
		saveDefaultConfig();

		try {
			loadUsers(getConfig().getString("data-file"));
		} catch (Exception e) {
			printError("There was an error loading user data!");
			e.printStackTrace();
		}

		initText();
		initCategories();

		registerCommandObject(new ItemFilterCommand(this));

		registerEventListener(new ItemPickupListener(this));
		registerEventListener(new PlayerJoinListener(this));
	}

	@Override
	public void onDisable() {
		try {
			saveUsers(getConfig().getString("data-file"));
		} catch (IOException e) {
			printError("There was an error saving user data!");
			e.printStackTrace();
		}
	}

	private void initCategories() {
		categories.clear();

		for (String category : getConfig().getConfigurationSection("categoriesGUI.categories").getKeys(false)) {
			String current = "categoriesGUI.categories." + category + ".";
			if (!getConfig().getBoolean(current + "enabled")) continue;

			int size = getConfig().getInt(current + "size");
			if (size == 0) size = 6;
			Material material = Material.valueOf(getConfig().getString(current + "material"));
			String name = getConfig().getString(current + "name");
			int slot = getConfig().getInt(current + "slot");
			String title = getConfig().getString(current + "title");
			ArrayList<Material> items = new ArrayList<>();
			for (String item : (List<String>) getConfig().getList(current + "items")) {
				items.add(Material.valueOf(item));
			}

			categories.add(new Category(size,material,name,slot,title,items));
		}
	}

	private void initText() {
		text.clear();

		for (String key : getConfig().getConfigurationSection("text").getKeys(false)) {
			if (key.equals("help")) {
				StringBuilder sText = new StringBuilder();

				for (String line : (ArrayList<String>) getConfig().getList("text." + key)) {
					line = line.replaceAll("&", "§");
					sText.append(line).append("\n");
				}

				text.put(key, sText.toString());
			} else {
				text.put(key, getConfig().getString("text." + key).replaceAll("&", "§"));
			}
		}
	}

	private void saveUsers(String fileS) throws IOException {
		File file = new File(fileS);
		if (!file.exists()) file.createNewFile();

		FileWriter fileWriter = new FileWriter(file);

		Gson gson = new Gson();
		fileWriter.write(gson.toJson(users));

		fileWriter.close();
	}

	private void loadUsers(String fileS) throws IOException {
		File file = new File(fileS);

		if (!file.exists()) {
			file.createNewFile();
			users = new HashSet<>();
			return;
		}

		FileReader fileReader = new FileReader(file);

		Type setType = new TypeToken<HashSet<User>>(){}.getType();
		users = new Gson().fromJson(fileReader, setType);

		if (users == null) users = new HashSet<>();
	}

	public User getUser(Player player) {
		for (User user : users) {
			if (user.getPlayer() == player) return user;
		}

		addUser(player);
		for (User user : users) {
			if (user.getPlayer() == player) return user;
		}

		return null;
	}


	public void addUser(Player player) {
		users.add(new User(player.getUniqueId(), new HashSet<>()));
	}

	public String getText(String id) {
		return text.getOrDefault(id, "TEXT COULD NOT BE LOADED");
	}
}
