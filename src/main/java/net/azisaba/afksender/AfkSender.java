package net.azisaba.afksender;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkSender extends JavaPlugin {
    private String serverName;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Load server name from config
        serverName = getConfig().getString("server-name", "lifeafk");

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(new AfkListener(this), this);
    }

    /**
     * Gets the configured AFK server name
     * @return The server name to send AFK players to
     */
    public String getServerName() {
        return serverName;
    }
}
