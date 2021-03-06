package zedly.createments;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Utilities {
    
    
/**
     * Checks if the given Block is any of the materials representing a type of
     * sign in 1.14.4.
     *
     * @param block
     * @return true if the block is a sign
     */
    public static boolean isSign(Block block) {
        return block != null && isMaterialSign(block.getType());
    }

    public static boolean isMaterialSign(final Material material) {
        switch (material) {
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
                return true;
            default:
                return false;
        }
    }
    // Displays a particle with the given data
    public static void display(Location loc, Particle particle, int amount, double speed, double xO, double yO, double zO) {
        loc.getWorld().spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), amount, (float) xO, (float) yO, (float) zO, (float) speed);
    }
    
    /**
     * Calculates the Location corresponding to the horizontal center of the
     * same block coordinates as the given location. The result has an
     * unaffected Y coordinate, but X and Z are in the center of the block.
     *
     * @param loc the location
     * @return the centered Location
     */
    public static Location getCenter(Location loc) {
        double x = loc.getX();
        double z = loc.getZ();
        if (x >= 0) {
            x += .5;
        } else {
            x += .5;
        }
        if (z >= 0) {
            z += .5;
        } else {
            z += .5;
        }
        Location lo = loc.clone();
        lo.setX(x);
        lo.setZ(z);
        return lo;
    }

    /**
     * Inserts color codes after each character in the input message, forming a
     * rainbow pattern. Starts at a randomly chosen point in the rainbow
     *
     * @param message The original message
     * @return The message with color codes inserted
     */
    public static String applyRainbowColors(String message) {
        int it = Storage.rnd.nextInt(12);
        char[] msg = message.toCharArray();
        String m = "";
        for (char s : msg) {
            if (!m.endsWith("\u00A7")) {
                m += Storage.textrainbow[it = (it + 1) % 12];
            }
            m += s;
        }
        return m;
    }

    /**
     * Removes one or more items with the specified properties from the given
     * inventory. Item stacks consisting of a single item will be set to null.
     *
     * @param inv the inventory to edit
     * @param mat the material of the items to remove
     * @param data the durability value of the items to remove
     * @param amount the number of such items to remove
     * @return the number of items actually removed.
     */
    public static int removeItem(Inventory inv, Material mat, short data, int amount) {
        int itemsRemoved = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() == mat && inv.getItem(i).getDurability() == data) {
                if (inv.getItem(i).getAmount() > amount) {
                    int res = inv.getItem(i).getAmount() - amount;
                    ItemStack rest = inv.getItem(i);
                    rest.setAmount(res);
                    inv.setItem(i, rest);
                    itemsRemoved += amount;
                    break;
                } else {
                    itemsRemoved += inv.getItem(i).getAmount();
                    amount -= inv.getItem(i).getAmount();
                    inv.setItem(i, null);
                }
            }
        }
        return itemsRemoved;
    }

    /**
     * Removes at most the desired amount of the item held in the given player's
     * hand.
     *
     * @param inv the player's inventory
     * @param slot the EquipmentSlot representing the player's hand
     * @param toRemove the amount of items to remove
     * @return the actual number of items removed
     */
    public static int removeItem(PlayerInventory inv, EquipmentSlot slot, int toRemove) {
        if (null == slot) {
            return 0;
        } else {
            switch (slot) {
                case HAND: {
                    if (inv.getItemInMainHand() == null || inv.getItemInMainHand().getType() == Material.AIR) {
                        return 0;
                    }
                    int present = inv.getItemInMainHand().getAmount();
                    if (present <= toRemove) {
                        inv.setItemInMainHand(new ItemStack(Material.AIR, 1));
                        return present;
                    } else {
                        present -= toRemove;
                        ItemStack is = inv.getItemInMainHand();
                        is.setAmount(present);
                        inv.setItemInMainHand(is);
                        return toRemove;
                    }
                }
                case OFF_HAND: {
                    if (inv.getItemInOffHand() == null || inv.getItemInOffHand().getType() == Material.AIR) {
                        return 0;
                    }
                    int present = inv.getItemInOffHand().getAmount();
                    if (present <= toRemove) {
                        inv.setItemInOffHand(new ItemStack(Material.AIR, 1));
                        return present;
                    } else {
                        present -= toRemove;
                        ItemStack is = inv.getItemInOffHand();
                        is.setAmount(present);
                        inv.setItemInOffHand(is);
                        return toRemove;
                    }
                }
                default:
                    return 0;
            }
        }
    }

    /**
     * Places a block on the given player's behalf. Fires a BlockPlaceEvent with
     * (nearly) appropriate parameters to probe the legitimacy (permissions etc)
     * of the action and to communicate to other plugins where the block is
     * coming from.
     *
     * @param player the player whose identity to use
     * @param mat the material to set the block to, if allowed
     * @param blockData the block data to set for the block, if allowed
     * @param blockAgainst the block against which the new block should be
     * placed. CAN be air, but this might cause other plugins to malfunction
     * @param blockFace the face of blockAgainst on which the new block will be
     * placed. This also determines the location of the new block
     * @param itemHeld the item the player is declared to be holding while the
     * block is placed. Ideally, this should reflect the material of the block
     * being placed.
     * @return true if the block placement has been successful
     */
    public static boolean attemptBlockPlacementAsPlayer(Player player, Material mat, int blockData, Block blockAgainst, BlockFace blockFace, ItemStack itemHeld) {
        BlockPlaceEvent placeEvent = new BlockPlaceEvent(blockAgainst.getRelative(blockFace), blockAgainst.getState(), blockAgainst, itemHeld, player, true);
        Bukkit.getPluginManager().callEvent(placeEvent);
        if (!placeEvent.isCancelled()) {
            Block relativeBlock = blockAgainst.getRelative(blockFace);
            relativeBlock.setType(mat);
            return true;
        }
        return false;
    }

    /**
     * Match an item stack against a number of criteria. Eliminates huge chains
     * of null checks
     *
     * @param is the item stack to match
     * @param mat the material to look for. null if irrelevant
     * @param durability the damage value to look for. -1 if irrelevant
     * @param name the name the item must have. null if irrelevant
     * @param lore a line of lore that must be contained in the item stack. null
     * if irrelevant
     * @return true if the item stack matches all specified criteria
     */
    public static boolean matchItemStack(ItemStack is, Material mat, String name, String lore) {
        if (is == null) {
            return false;
        }
        if (mat != null && is.getType() != mat) {
            return false;
        }
        if ((name != null || lore != null) && !is.hasItemMeta()) {
            return false;
        }
        if (name != null && !(is.getItemMeta().hasDisplayName() || !is.getItemMeta().getDisplayName().equals(name))) {
            return false;
        }
        if (lore != null) {
            if (is.getItemMeta().hasLore()) {
                for (String loreLine : is.getItemMeta().getLore()) {
                    if (loreLine.equals(lore)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Adds a potion effect of given length and intensity to the given entity.
     *
     * @param ent the entity to affect
     * @param type the potion effect type
     * @param length duration, in ticks, of the potion effect
     * @param intensity the level of the potion effect
     *
     */
    public static void addPotion(LivingEntity ent, PotionEffectType type, int length, int intensity) {
        for (PotionEffect eff : ent.getActivePotionEffects()) {
            if (eff.getType().equals(type)) {
                if (eff.getAmplifier() > intensity) {
                    return;
                } else if (eff.getDuration() > length) {
                    return;
                } else {
                    ent.removePotionEffect(type);
                }
            }
        }
        ent.addPotionEffect(new PotionEffect(type, length, intensity));
    }

    public static boolean attackEntity(LivingEntity target, Player attacker, double damage) {
        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(attacker, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
        Bukkit.getPluginManager().callEvent(damageEvent);
        if (!damageEvent.isCancelled()) {
            target.damage(damage);
            return true;
        }
        return false;
    }
}
