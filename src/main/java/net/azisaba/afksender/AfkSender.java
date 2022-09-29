package net.azisaba.afksender;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkSender extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(new AfkListener(this), this);
    }
}
