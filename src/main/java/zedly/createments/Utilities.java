package zedly.createments;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.*;

public class Utilities {

    /**
     * Calculates the Location corresponding to the horizontal center of the same block coordinates
     * as the given location. 
     * The result has an unaffected Y coordinate, but X and Z are in the center of the block.
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
            if (!m.endsWith("§")) {
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
            relativeBlock.setData((byte) blockData);
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
    public static boolean matchItemStack(ItemStack is, Material mat, int durability, String name, String lore) {
        if (is == null) {
            return false;
        }
        if (mat != null && is.getType() != mat) {
            return false;
        }
        if (durability != -1 && is.getDurability() != durability) {
            return false;
        }
        if ((name != null || lore != null) && !is.hasItemMeta()) {
            return false;
        }
        if (name != null && !(is.getItemMeta().hasDisplayName() || !is.getItemMeta().getDisplayName().equals(name))) {
            return false;
        }
        if (lore != null && !(is.getItemMeta().hasLore() || !is.getItemMeta().getLore().contains(lore))) {
            return false;
        }
        return true;
    }

}
