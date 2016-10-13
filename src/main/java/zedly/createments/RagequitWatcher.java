/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zedly.createments;

import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author Dennis
 */
public class RagequitWatcher implements Listener {

    private static final HashSet<Player> ragequits = new HashSet<>();

    @EventHandler // Death Charms
    public void onPlayerDeath(PlayerDeathEvent evt) {
        if (evt.getEntity() instanceof Player) {
            ragequits.add((Player) evt.getEntity());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if (ragequits.contains(evt.getPlayer())) {
            evt.setQuitMessage(ChatColor.RED + evt.getPlayer().getName() + " rage quit!");
        }
        ragequits.remove(evt.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent evt) {
        ragequits.remove(evt.getPlayer());
    }

}
