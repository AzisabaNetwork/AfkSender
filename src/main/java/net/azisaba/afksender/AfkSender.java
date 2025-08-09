package net.azisaba.afksender;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public final class AfkSender extends JavaPlugin {
    private List<String> serverName;
    private boolean cancelAfkOnRotate;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Load server name from config
        if (getConfig().isList("server-name")) {
            serverName = getConfig().getStringList("server-name");
        } else {
            serverName = Collections.singletonList(getConfig().getString("server-name"));
        }
        cancelAfkOnRotate = getConfig().getBoolean("cancel-afk-on-rotate", true);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(new AfkListener(this), this);
    }

    /**
     * Gets the configured AFK server name
     * @return The server name to send AFK players to
     */
    public List<String> getServerName() {
        return serverName;
    }

    public boolean isCancelAfkOnRotate() {
        return cancelAfkOnRotate;
    }
}
