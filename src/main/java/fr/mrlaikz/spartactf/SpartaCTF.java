package fr.mrlaikz.spartactf;

import fr.mrlaikz.spartactf.commands.CTFCommand;
import fr.mrlaikz.spartactf.listeners.*;
import fr.mrlaikz.spartactf.managers.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SpartaCTF extends JavaPlugin {

    private static SpartaCTF INSTANCE;

    private EventManager eventManager;

    @Override
    public void onEnable() {
        //VARS
        saveDefaultConfig();
        INSTANCE = this;

        //MANAGERS
        eventManager = new EventManager(this);

        //LISTENERS
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);

        //COMMANDS
        getCommand("ctf").setExecutor(new CTFCommand(this));

        //MISC
        getLogger().info("Plugin Actif");
    }

    @Override
    public void onDisable() {
       getLogger().info("Plugin Innactif");
    }

    //CONFIG
    public static SpartaCTF getInstance() {
        return INSTANCE;
    }

    public static String strConfig(String path) {
        return ChatColor.translateAlternateColorCodes('&', INSTANCE.getConfig().getString(path));
    }



    //MANAGERS
    public EventManager getEventManager() {
        return eventManager;
    }

}
