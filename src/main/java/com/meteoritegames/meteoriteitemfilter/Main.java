package com.meteoritegames.meteoriteitemfilter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meteoritegames.meteoriteitemfilter.commands.ItemFilterCommand;
import com.meteoritegames.meteoriteitemfilter.listeners.ItemPickupListener;
import com.meteoritegames.meteoriteitemfilter.objects.User;
import com.meteoritepvp.api.MeteoritePlugin;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main extends MeteoritePlugin {
	private static Set<User> users;
	public HashMap<String, String> text = new HashMap<>();

	@Override
	protected void onInit() {
		super.onInit();
		saveDefaultConfig();

		try {
			loadUsers("userData.json");
		} catch (Exception e) {
			printError("There was an error loading user data! Plugin: ItemFilter");
			e.printStackTrace();
		}

		initText();
		registerCommandObject(new ItemFilterCommand(this));

		registerEventListener(new ItemPickupListener(this));
	}

	@Override
	public void onDisable() {
		try {
			saveUsers("userData.json");
		} catch (IOException e) {
			printError("There was an error loading user data! Plugin: ItemFilter");
			e.printStackTrace();
		}
	}

	private void initText() {
		text.clear();

		for (String key : getConfig().getConfigurationSection("text").getKeys(false)) {
			text.put(key, getConfig().getString("text." + key).replaceAll("&", "§"));
		}
	}

	private void saveUsers(String fileS) throws IOException {
		File file = new File(fileS);

		FileWriter fileWriter = new FileWriter(file);

		Gson gson = new Gson();
		fileWriter.write(gson.toJson(users));
	}

	private void loadUsers(String fileS) throws FileNotFoundException {
		File file = new File(fileS);

		if (!file.exists()) {
			users = new HashSet<>();
			return;
		}

		FileReader fileReader = new FileReader(file);

		Type setType = new TypeToken<HashSet<User>>(){}.getType();
		users = new Gson().fromJson(fileReader, setType);
	}

	public User getUser(Player player) {
		for (User user : users) {
			if (user.getPlayer() == player) return user;
		}

		return null;
	}

	public String getText(String id) {
		return text.getOrDefault(id, "TEXT COULD NOT BE LOADED");
	}
}
