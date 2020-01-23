package zedly.createments;

import zedly.createments.projectiles.*;
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

    private final HashMap<Location, String> advancedFireballsDispensing = new HashMap<>();

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

    @EventHandler // Advanced Projectiles in Dispensers & Paragraphers
    public void onParticleDispenser(final BlockDispenseEvent evt) {
        ItemStack stk = (ItemStack) evt.getItem();

        // Advanced Projectiles in Dispensers
        if (AdvancedProjectile.isAdvancedFireball(stk)) {
            advancedFireballsDispensing.put(evt.getBlock().getLocation(), stk.getItemMeta().getLore().get(0));
            return;
        }
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
        if (evt.getAction() == RIGHT_CLICK_AIR
                && Utilities.matchItemStack(evt.getPlayer().getInventory().getItemInMainHand(), Material.FIRE_CHARGE, null, null)
                && evt.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            ItemStack is = evt.getPlayer().getInventory().getItemInMainHand();
            if (AdvancedProjectile.isAdvancedFireball(is)) {
                SmallFireball sf = (SmallFireball) evt.getPlayer().getWorld().spawnEntity(evt.getPlayer().getLocation().add(new Vector(0, 1.62, 0)).add(evt.getPlayer().getLocation().getDirection().multiply(2.5)), EntityType.SMALL_FIREBALL);
                sf.setVelocity(evt.getPlayer().getLocation().getDirection().multiply(1.5));
                sf.setIsIncendiary(false);
                AdvancedProjectile ap = AdvancedProjectile.create(is, sf);
                Storage.advancedProjectiles.put(sf, ap);
                if (is.getAmount() == 1) {
                    evt.getPlayer().getInventory().setItemInMainHand(new ItemStack(AIR));
                } else {
                    is.setAmount(is.getAmount() - 1);
                    evt.getPlayer().getInventory().setItemInMainHand(is);
                }
            }
        } else if ((evt.getAction() == Action.RIGHT_CLICK_BLOCK || evt.getAction() == Action.RIGHT_CLICK_AIR && evt.getHand() == EquipmentSlot.HAND)
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

    @EventHandler // Advanced Projectiles & Lore Bows
    public void onProjectileHit(ProjectileHitEvent evt) {
        // Advanced Projectiles
        if (Storage.advancedProjectiles.containsKey(evt.getEntity())) {
            Storage.advancedProjectiles.get(evt.getEntity()).impact();
            Storage.advancedProjectiles.remove(evt.getEntity());
        }
    }

    @EventHandler // Advanced Projectiles in Dispensers
    public void onProjectileLaunch(ProjectileLaunchEvent evt) {
        Set<Location> toDie = new HashSet<>();
        for (Location l : advancedFireballsDispensing.keySet()) {
            if (l.distance(evt.getEntity().getLocation()) < 2) {
                String loreString = advancedFireballsDispensing.get(l);
                AdvancedProjectile ap = AdvancedProjectile.create(loreString, (SmallFireball) evt.getEntity());
                Storage.advancedProjectiles.put(evt.getEntity(), ap);
                toDie.add(l);
            }
        }
        for (Location l : toDie) {
            advancedFireballsDispensing.remove(l);
        }
    }
}
