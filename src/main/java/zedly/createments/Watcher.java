package zedly.createments;

import java.util.*;
import java.util.Map.Entry;
import org.bukkit.*;
import static org.bukkit.Material.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import static org.bukkit.event.block.Action.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.util.Vector;

public class Watcher implements Listener {

    
    @EventHandler // Rainbow Chat
    public void onChat(AsyncPlayerChatEvent evt) {
        String text = evt.getMessage();
        if (evt.getPlayer().hasPermission("createments.emoticons")) {
            for (Entry<String, String> sub : Storage.emoticonSubstitutions.entrySet()) {
                text = text.replace(sub.getKey(), sub.getValue());
            }
        }

        if (text.matches("(.*)\\b[tT][hH][eE] [bB][oO][xX]\\b(.*)")) {
            if (evt.getPlayer().hasPermission("createments.thebox")) {
                text = text.replaceAll("\\b[tT][hH][eE] [bB][oO][xX]\\b", ChatColor.DARK_RED + "The Box" + ChatColor.RESET);
                for (Player p : Storage.createments.getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 5, 1);
                }
            }
        }

        synchronized (Storage.rainbowplayers) {
            if (Storage.rainbowplayers.contains(evt.getPlayer())) {
                text = Utilities.applyRainbowColors(text);
            }
        }
        evt.setMessage(text);
    }

    

    @EventHandler
    public void onEntityDeath(EntityDeathEvent evt) {
        if (!(evt.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        EntityDamageByEntityEvent eevt = (EntityDamageByEntityEvent) evt.getEntity().getLastDamageCause();
        if (!(eevt.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) eevt.getDamager();
        ItemStack is = p.getInventory().getItemInMainHand();
        if (is.getType() == Material.AIR || !is.hasItemMeta() || !is.getItemMeta().hasLore()) {
            return;
        }
        ItemMeta meta = is.getItemMeta();
        List<String> lore = is.getItemMeta().getLore();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.matches(ChatColor.GOLD + "Kills: \\d+")) {
                int kills = Integer.parseInt(line.substring(9));
                kills++;
                lore.set(i, ChatColor.GOLD + "Kills: " + kills);
                break;
            }
        }
        meta.setLore(lore);
        is.setItemMeta(meta);
        p.getInventory().setItemInMainHand(is);
        p.updateInventory();
    }

    @EventHandler // Advanced Projectiles & Baseballs
    public void onInteract(PlayerInteractEvent evt) {
        // Advanced Projectiles
        if ((evt.getAction() == Action.RIGHT_CLICK_BLOCK || evt.getAction() == Action.RIGHT_CLICK_AIR && evt.getHand() == EquipmentSlot.HAND)
                && evt.getPlayer().isSneaking()
                && Utilities.matchItemStack(evt.getPlayer().getInventory().getItemInMainHand(), Material.PAPER, null, ChatColor.GOLD + "Kill Counter")) {

            ItemStack is = evt.getPlayer().getInventory().getItemInOffHand();
            if (is == null || is.getType() == Material.AIR) {
                evt.getPlayer().sendMessage(ChatColor.GRAY + "Hold an item in your off hand to add a Kill Counter!");
                return;
            }

            ItemMeta meta = is.getItemMeta();
            List<String> lore = is.getItemMeta().getLore();
            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.get(i);
                    if (line.matches(ChatColor.GOLD + "Kills: \\d+")) {
                        evt.getPlayer().sendMessage(ChatColor.GRAY + "This item already has a Kill Counter!");
                        return;
                    }
                }
            } else {
                lore = new ArrayList<>();
            }
            lore.add(ChatColor.GOLD + "Kills: 0");
            meta.setLore(lore);
            is.setItemMeta(meta);
            evt.getPlayer().getInventory().setItemInOffHand(is);
            Utilities.removeItem(evt.getPlayer().getInventory(), EquipmentSlot.HAND, 1);
            Utilities.display(evt.getPlayer().getEyeLocation(), Particle.CRIT, 100, 0.3, 3, 3, 3);
            evt.getPlayer().sendMessage(ChatColor.GRAY + "Kill Counter added");
            evt.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void baseball(PlayerInteractEvent evt) {
        if (evt.getAction() == LEFT_CLICK_AIR && evt.getPlayer().getInventory().getItemInMainHand().getType() == STICK) {
            ItemStack is = evt.getPlayer().getInventory().getItemInMainHand();
            if (is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(ChatColor.GRAY + "Baseball Bat")) {
                List<Entity> ents = evt.getPlayer().getNearbyEntities(3, 3, 3);
                double force = Double.parseDouble(is.getItemMeta().getLore().get(1));
                for (Entity ent : ents) {
                    if (ent instanceof Projectile) {
                        Vector face = evt.getPlayer().getLocation().getDirection();
                        Vector r = ent.getVelocity().subtract(face.multiply(force));
                        ent.setVelocity(r.multiply(-1));
                    }
                }
            }
        }
    }
}
