package zedly.createments;

import zedly.createments.projectiles.*;
import java.util.*;
import java.util.Map.Entry;
import net.objecthunter.exp4j.*;
import org.bukkit.*;
import static org.bukkit.GameMode.*;
import static org.bukkit.Material.*;
import org.bukkit.block.*;
import static org.bukkit.block.BlockFace.*;
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

    private static final HashSet<Material> BLOCK_FILTER_AIR_ONLY = new HashSet<>();
    private final HashMap<Location, String> advancedFireballsDispensing = new HashMap<>();

    static {
        BLOCK_FILTER_AIR_ONLY.add(Material.AIR);
    }

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

        // Paragraphers
        if (evt.getBlock().getType() != DISPENSER) {
            return;
        }

        if (stk == null || stk.getType() != BOOK_AND_QUILL || !stk.hasItemMeta() || !stk.getItemMeta().hasLore()) {
            return;
        }
        if (!stk.getItemMeta().getLore().get(0).equals(ChatColor.YELLOW + "Paragrapher")) {
            return;
        }
        evt.setCancelled(true);
        BookMeta bm = (BookMeta) stk.getItemMeta();
        String fX = ChatColor.stripColor(bm.getPage(1));
        String fY = ChatColor.stripColor(bm.getPage(2));
        String fZ = ChatColor.stripColor(bm.getPage(3));
        String tLow = ChatColor.stripColor(bm.getPage(4));
        String tHigh = ChatColor.stripColor(bm.getPage(5));
        String tInc = ChatColor.stripColor(bm.getPage(6));
        String tTime = ChatColor.stripColor(bm.getPage(7));

        if (bm.getPageCount() < 7 || !fX.startsWith("Equation for X:") || !fY.startsWith("Equation for Y:") || !fZ.startsWith("Equation for Z:")) {
            evt.getBlock().getLocation().getWorld().spigot().playEffect(Utilities.getCenter(evt.getBlock().getLocation()), Effect.CLOUD, 0, 1, 0f, 0f, 0f, .1f, 100, 32);
            return;
        }
        if (!tLow.startsWith("t Min:") || !tHigh.startsWith("t Max:") || !tInc.startsWith("t Increment:") || !tTime.startsWith("t Delay:")) {
            evt.getBlock().getLocation().getWorld().spigot().playEffect(Utilities.getCenter(evt.getBlock().getLocation()), Effect.VILLAGER_THUNDERCLOUD, 0, 1, 0f, 0f, 0f, .1f, 100, 32);
            return;
        }

        fX = fX.substring(16).replace("\n", "");
        fY = fY.substring(16).replace("\n", "");
        fZ = fZ.substring(16).replace("\n", "");
        tLow = tLow.substring(7);
        tHigh = tHigh.substring(7);
        tInc = tInc.substring(13);
        tTime = tTime.substring(9);

        int low, high, increment;
        double time;

        try {
            low = Integer.parseInt(tLow);
            high = Integer.parseInt(tHigh);
            increment = Integer.parseInt(tInc);
            time = Double.parseDouble(tTime);
        } catch (NumberFormatException ex) {
            evt.getBlock().getLocation().getWorld().spigot().playEffect(Utilities.getCenter(evt.getBlock().getLocation()), Effect.CLOUD, 0, 1, 0f, 0f, 0f, .1f, 100, 32);
            return;
        }

        if (low > high) {
            int t = low;
            low = high;
            high = t;
        }
        if (time < 0) {
            time = 0;
        }
        int q = 0;
        final Expression eX = new ExpressionBuilder(fX).variables("t").build();
        final Expression eY = new ExpressionBuilder(fY).variables("t").build();
        final Expression eZ = new ExpressionBuilder(fZ).variables("t").build();
        for (int i = low; i < high; i = i + increment) {
            q++;
            final float j = i;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.createments, new Runnable() {
                public void run() {
                    Location loc = Utilities.getCenter(evt.getBlock().getLocation());
                    loc.setX(loc.getX() + eX.setVariable("t", j).evaluate());
                    loc.setY(loc.getY() + eY.setVariable("t", j).evaluate());
                    loc.setZ(loc.getZ() + eZ.setVariable("t", j).evaluate());
                    evt.getBlock().getLocation().getWorld().spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 1, 0, 0, 0, 10f, 1, 32);
                }
            }, (int) (q * time + 1));
        }
    }

    @EventHandler
    public void onPlayerEditBook(final PlayerEditBookEvent evt) {
        if (evt.getPreviousBookMeta() != null && evt.getPreviousBookMeta().getLore().contains(ChatColor.YELLOW + "Paragrapher")) {
            BookMeta m = evt.getNewBookMeta().clone();
            m.setLore(evt.getPreviousBookMeta().getLore());
            evt.setNewBookMeta(m);
        }
    }

    @EventHandler // Advanced Projectiles & Baseballs
    public void onInteract(PlayerInteractEvent evt) {
        // Advanced Projectiles
        if (evt.getAction() == RIGHT_CLICK_AIR && evt.getPlayer().getInventory().getItemInMainHand() != null) {
            if (Utilities.matchItemStack(evt.getPlayer().getInventory().getItemInMainHand(), Material.FIREBALL, -1, null, null)) {
                if (evt.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    ItemStack is = evt.getPlayer().getInventory().getItemInMainHand();
                    if (AdvancedProjectile.isAdvancedFireball(is)) {
                        SmallFireball sf = (SmallFireball) evt.getPlayer().getWorld().spawnEntity(evt.getPlayer().getLocation().add(new Vector(0, 1.62, 0)).add(evt.getPlayer().getLocation().getDirection().multiply(2.5)), EntityType.SMALL_FIREBALL);
                        sf.setVelocity(evt.getPlayer().getLocation().getDirection().multiply(1.5));
                        sf.setIsIncendiary(false);
                        AdvancedProjectile ap = AdvancedProjectile.create(is, sf);
                        Storage.advancedProjectiles.put(sf, ap);
                        if (is.getAmount() == 1) {
                            evt.getPlayer().setItemInHand(new ItemStack(0, 0));
                        } else {
                            is.setAmount(is.getAmount() - 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void baseball(PlayerInteractEvent evt) {
        if (evt.getAction() == LEFT_CLICK_AIR && evt.getPlayer().getInventory().getItemInMainHand() != null
                && evt.getPlayer().getInventory().getItemInMainHand().getType() == STICK) {
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

    @EventHandler(ignoreCancelled = false) // Water Bonemeal
    public void onWaterBonemeal(PlayerInteractEvent evt) throws Exception {
        if ((evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (Utilities.matchItemStack(evt.getPlayer().getInventory().getItemInMainHand(), Material.INK_SACK, 15, null, null)) {
                Block targetedBlock = evt.getPlayer().getTargetBlock(BLOCK_FILTER_AIR_ONLY, 4);
                if (targetedBlock.getType() == Material.STATIONARY_WATER
                        && targetedBlock.getRelative(0, 1, 0).getType() == AIR) {

                    BlockBreakEvent event = new BlockBreakEvent(evt.getPlayer().getLocation().getBlock(), evt.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    boolean liliesCreated = false;
                    for (int x = -4; x <= 4; x++) {
                        for (int z = -4; z <= 4; z++) {
                            if (Storage.rnd.nextInt(15) != 12) {
                                continue;
                            }
                            Block possibleLilyBlock = targetedBlock.getRelative(x, 1, z);
                            if (possibleLilyBlock.getLocation().distanceSquared(targetedBlock.getLocation()) < 12) {
                                if (possibleLilyBlock.getType() == AIR && possibleLilyBlock.getRelative(0, -1, 0).getType() == STATIONARY_WATER) {
                                    if (Utilities.attemptBlockPlacementAsPlayer(evt.getPlayer(), Material.WATER_LILY, 0, targetedBlock.getRelative(x, 0, z), BlockFace.UP, new ItemStack(Material.WATER_LILY))) {
                                        evt.getPlayer().getWorld().spigot().playEffect(Utilities.getCenter(possibleLilyBlock.getLocation()), Effect.HAPPY_VILLAGER, 0, 1, .3f, .3f, .3f, 1f, 20, 16);
                                        liliesCreated = true;
                                    }
                                }
                            }
                        }
                    }
                    if (liliesCreated && !evt.getPlayer().getGameMode().equals(CREATIVE)) {
                        Utilities.removeItem(evt.getPlayer().getInventory(), Material.INK_SACK, (short) 15, 1);
                    }
                }
            }
        }
    }

    @EventHandler // 321 Explosive Items
    public void onItemPickup(PlayerPickupItemEvent evt) {
        ItemStack stk = evt.getItem().getItemStack();
        if (stk != null && stk.hasItemMeta() && stk.getItemMeta().hasLore()
                && stk.getItemMeta().getLore().contains("321")) {
            Location l = evt.getItem().getLocation();
            evt.getItem().getLocation().getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 25, false, false);
        }
    }

    @EventHandler // Advanced Projectiles & Lore Bows
    public void onProjectileHit(ProjectileHitEvent evt) {
        // Advanced Projectiles
        if (Storage.advancedProjectiles.containsKey(evt.getEntity())) {
            Storage.advancedProjectiles.get(evt.getEntity()).impact();
            Storage.advancedProjectiles.remove(evt.getEntity());
        }
        // Lore Bows
        int d = 0;
        ArrayList<String> signData = new ArrayList<>();
        if (evt.getEntity().getShooter() instanceof Player) {
            Player p = (Player) evt.getEntity().getShooter();
            if (Utilities.matchItemStack(p.getInventory().getItemInMainHand(),
                    Material.BOW, -1, null, ChatColor.GOLD + "Lore Bow")) {
                evt.getEntity().remove();
                signData.addAll(p.getInventory().getItemInMainHand().getItemMeta().getLore());
                signData.remove(0);
                float direction = p.getLocation().getYaw();
                if (direction < 0) {
                    direction += 360;
                }
                direction += 8;
                direction /= 22.5;
                d = (int) direction;
                d -= (d > 7) ? 8 : -8;
            }
        }
        float[] floats = new float[]{2f, 1.75f, 1.5f, 1.25f, 1f, .75f, .5f, .25f, .1f, .01f};
        for (float i : floats) {
            final Location hitLoc = evt.getEntity().getLocation().clone().add(evt.getEntity().getVelocity().multiply(.25 * i));
            if (hitLoc.getBlock().getType().equals(AIR) && !signData.isEmpty()) {
                double xDif = evt.getEntity().getLocation().getX() - hitLoc.getX();
                double zDif = evt.getEntity().getLocation().getZ() - hitLoc.getZ();
                byte blk = 0;
                BlockFace[] blks = new BlockFace[]{SOUTH, NORTH, EAST, WEST};
                for (BlockFace b : blks) {
                    if (!hitLoc.getBlock().getRelative(b).getType().equals(AIR)) {
                        blk = (byte) (ArrayUtils.indexOf(blks, b) + 2);
                    }
                }
                if (Math.abs(xDif) > Math.abs(zDif)) {
                    if (xDif > .1) {
                        blk = 5;
                    } else if (xDif < -.1) {
                        blk = 4;
                    }
                } else if (zDif > .1) {
                    blk = 3;
                } else if (zDif < -.1) {
                    blk = 2;
                }
                BlockBreakEvent event = new BlockBreakEvent(hitLoc.getBlock(), (Player) evt.getEntity().getShooter());
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                Material[] blacklist = new Material[]{AIR, SIGN_POST, WALL_SIGN};
                if (blk != 0 && !ArrayUtils.contains(blacklist, hitLoc.getBlock().getRelative(blks[blk - 2]).getType())) {
                    hitLoc.getBlock().setType(WALL_SIGN);
                    hitLoc.getBlock().setData(blk);
                }
                if (!hitLoc.getBlock().getRelative(DOWN).getType().equals(AIR) && hitLoc.getBlock().getType() != WALL_SIGN && hitLoc.getBlock().getType() != SIGN_POST) {
                    hitLoc.getBlock().setType(SIGN_POST);
                    hitLoc.getBlock().setData((byte) d);
                }
                if (hitLoc.getBlock().getType() == SIGN_POST || hitLoc.getBlock().getType() == WALL_SIGN) {
                    long l = 10;
                    Storage.decay.put(hitLoc.getBlock(), false);
                    Sign sign = (Sign) hitLoc.getBlock().getState();
                    for (int x = 0; x < 4; x++) {
                        if (signData.size() >= x + 1) {
                            sign.setLine(x, signData.get(x));
                        }
                    }
                    sign.update();
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.createments, new Runnable() {
                        public void run() {
                            if (hitLoc.getBlock().getType() == SIGN_POST || hitLoc.getBlock().getType() == WALL_SIGN) {
                                Storage.decay.put(hitLoc.getBlock(), true);
                            } else {
                                Storage.decay.remove(hitLoc.getBlock());
                            }
                        }
                    }, l * 20);
                }
                break;
            }
        }
    }

    @EventHandler // Advanced Projectiles in Dispensers
    public void onProjectileLaunch(ProjectileLaunchEvent evt) {
        if (evt.getEntity() != null) {
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
}
