package net.azisaba.afksender;

import com.earth2me.essentials.Essentials;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class AfkListener implements Listener {
    private final AfkSender plugin;
    private Essentials ess;

    public AfkListener(AfkSender plugin) {
        this.plugin = plugin;
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!plugin.isCancelAfkOnRotate()) {
            return;
        }

        Player player = e.getPlayer();
        // To prevent players from abusing the water flow, we check the rotation change
        // This only works if you set cancelAfkOnMove=false in the essential config
        if (ess != null && e.getTo() != null && e.getFrom().getYaw() != e.getTo().getYaw()) {
            ess.getUser(player).updateActivity(false, AfkStatusChangeEvent.Cause.MOVE);
        } else if (ess == null) {
            ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        }
    }

    @EventHandler
    public void onAfkStatusChange(AfkStatusChangeEvent e) {
        // Return if player is back from AFK
        if (!e.getValue()) {
            return;
        }

        // Do not send the player to the afk if they have permission to bypass it
        if (e.getAffected().getBase().hasPermission("afksender.bypass")) {
            return;
        }

        if (plugin.getServerName().isEmpty()) {
            return;
        }

        // send message ...
        e.getAffected().getBase().sendMessage(ChatColor.GOLD + "離席状態になったため、自動的にAFKサーバーに転送されました。");

        for (int i = 0; i < plugin.getServerName().size(); i++) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!e.getAffected().getBase().isOnline()) {
                    return;
                }
                try {
                    // ... create byte array ...
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try (DataOutputStream out = new DataOutputStream(baos)) {
                        out.writeUTF("Connect");
                        out.writeUTF(plugin.getServerName().get(finalI)); // server name from config
                    }

                    // ... and send the packet
                    e.getAffected().getBase().sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.WARNING, "Failed to send player to AFK server", ex);
                }
            }, i * 20L);
        }
    }
}
