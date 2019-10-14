/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zedly.createments;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Spawns the enchanting table particle effect around the user. 
 * This is a vanity feature for devs who are "AFK" writing code.
 * @author Dennis
 */
public class Aura implements Runnable {

    private static final HashMap<Player, Aura> auras = new HashMap<>();

    private final Player player;
    private int id;

    public static boolean isEnabledFor(Player player) {
        return auras.containsKey(player);
    }

    public static void enableFor(Player player) {
        if (!auras.containsKey(player)) {
            Aura au = new Aura(player);
            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storage.createments, au, 0, 5);
            au.identify(id);
            auras.put(player, au);
        }
    }

    public static void disableFor(Player player) {
        if (auras.containsKey(player)) {
            auras.get(player).stop();
            auras.remove(player);
        }
    }

    private Aura(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            stop();
            return;
        }
        Utilities.display(player.getLocation().add(new Vector(0, 2, 0)), Particle.ENCHANTMENT_TABLE, 10, 1.0, 0, 0, 3);
    }

    private void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }

    private synchronized void identify(int id) {
        this.id = id;
    }
}
