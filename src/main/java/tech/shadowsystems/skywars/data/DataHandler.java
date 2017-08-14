package tech.shadowsystems.skywars.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.shadowsystems.skywars.Skywars;

import java.io.File;
import java.io.IOException;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class DataHandler {
    private static DataHandler ourInstance = new DataHandler();
    public static DataHandler getInstance() {
        return ourInstance;
    }
    private DataHandler() {
        this.gameInfoFile = new File(Skywars.getInstance().getDataFolder(), "gameInfo.yml");
        if (!this.gameInfoFile.exists()) {
            try {
                this.gameInfoFile.getParentFile().mkdirs();
                this.gameInfoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.gameInfo = YamlConfiguration.loadConfiguration(this.gameInfoFile);
    }

    private File gameInfoFile;
    private FileConfiguration gameInfo;

    public FileConfiguration getGameInfo() {
        return gameInfo;
    }

    public void saveGameInfo() {
        try {
            this.gameInfo.save(this.gameInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
