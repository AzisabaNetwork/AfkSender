package net.azisaba.afksender;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AfkListener implements Listener {
    private final AfkSender plugin;

    public AfkListener(AfkSender plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAfkStatusChange(AfkStatusChangeEvent e) throws IOException {
        // Return if player is back from AFK
        if (!e.getValue()) {
            return;
        }

        // Do not send the player to the afk if they have permission to bypass it
        if (e.getAffected().getBase().hasPermission("afksender.bypass")) {
            return;
        }

        // send message ...
        e.getAffected().getBase().sendMessage(ChatColor.GOLD + "離席状態になったため、自動的にAFKサーバーに転送されました。");

        // ... create byte array ...
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            out.writeUTF("Connect");
            out.writeUTF("lifeafk"); // server name (hardcoded currently)
        }

        // ... and send the packet
        e.getAffected().getBase().sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
    }
}
