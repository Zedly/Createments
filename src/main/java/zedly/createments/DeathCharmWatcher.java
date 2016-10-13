/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zedly.createments;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Dennis
 */
public class DeathCharmWatcher implements Listener {

    @EventHandler // Death Charms
    public void onPlayerDeath(PlayerDeathEvent evt) {
        for (ItemStack stk : evt.getDrops()) {
            if (Utilities.matchItemStack(stk, Material.PAPER, 0, null, null)) {
                if (stk.hasItemMeta() && 
                        stk.getItemMeta().hasLore() && 
                        stk.getItemMeta().hasDisplayName() && 
                        stk.getItemMeta().getLore().get(0) != null) {
                    if (stk.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Advanced Death Charm") && stk.getItemMeta().getLore().get(0).contains(Storage.BOX_CHAR + "")) {
                        int charge = getDeathCharmCharge(stk);
                        if (charge <= 0) {
                            continue;
                        }
                        setDeathCharmCharge(stk, charge - 1, 2);
                        evt.setKeepInventory(true);
                        evt.setKeepLevel(true);
                        break;
                    } else if (stk.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Basic Death Charm") && stk.getItemMeta().getLore().get(0).contains(Storage.BOX_CHAR + "")) {
                        int charge = getDeathCharmCharge(stk);
                        if (charge <= 0) {
                            continue;
                        }
                        setDeathCharmCharge(stk, charge - 1, 6);
                        ((Player) evt.getEntity()).sendMessage("You died at: X = " + evt.getEntity().getLocation().getBlockX() + "  Y = " + evt.getEntity().getLocation().getBlockY() + "  Z = " + evt.getEntity().getLocation().getBlockZ());
                        break;
                    }
                }
            }
        }
    }

    private int getDeathCharmCharge(ItemStack is) {
        if (!is.hasItemMeta() || !is.getItemMeta().hasLore()) {
            return 0;
        }
        List<String> lore = is.getItemMeta().getLore();
        char[] chars = lore.get(0).toCharArray();
        int charge = 0;
        boolean b = false;
        for (char c : chars) {
            if (c == 'a') {
                b = true;
            }
            if (c == 'c') {
                b = false;
            }
            if (c == Storage.BOX_CHAR && b) {
                charge++;
            }
        }
        return charge;
    }

    private void setDeathCharmCharge(ItemStack is, int charge, int capacity) {
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.DARK_GREEN);
        for (int i = 0; i < charge; i++) {
            sb.append(Storage.BOX_CHAR);
        }
        sb.append(ChatColor.DARK_RED);
        for (int i = 0; i < capacity - charge; i++) {
            sb.append(Storage.BOX_CHAR);
        }
        lore.set(0, sb.toString());
        meta.setLore(lore);
        is.setItemMeta(meta);
    }

}
