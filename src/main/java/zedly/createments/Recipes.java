package zedly.createments;

import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class Recipes {
    
    public static void apply() {
        deathCharm();
        advancedDeathCharm();
        cobweb();
        ropeLadder();
    }

    public static void deathCharm() {
        ItemStack paper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Basic Death Charm");
        List<String> lore = new ArrayList<>();
        lore.add("" + ChatColor.DARK_GREEN + Storage.BOX_CHAR + Storage.BOX_CHAR + Storage.BOX_CHAR + Storage.BOX_CHAR + Storage.BOX_CHAR);
        meta.setLore(lore);
        paper.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(paper));
        recipe.addIngredient(Material.PAPER).addIngredient(Material.COMPASS).addIngredient(Material.IRON_BLOCK).addIngredient(Material.COAL_BLOCK);
        Bukkit.addRecipe(recipe);
    }

    public static void advancedDeathCharm() {
        ItemStack paper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Advanced Death Charm");
        List<String> lore = new ArrayList<>();
        lore.add("" + ChatColor.DARK_GREEN + Storage.BOX_CHAR + Storage.BOX_CHAR);
        meta.setLore(lore);
        paper.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(paper));
        recipe.addIngredient(Material.PAPER).addIngredient(Material.COMPASS).addIngredient(Material.DIAMOND_BLOCK).addIngredient(Material.EMERALD_BLOCK);
        Bukkit.addRecipe(recipe);
    }
    
    public static void cobweb() {
        ItemStack cobweb = new ItemStack(Material.WEB, 1);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Storage.createments, "cobweb"), cobweb);
        recipe.shape("SAS", "ASA", "SAS");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('A', Material.AIR);
        Bukkit.addRecipe(recipe);
    }
    
    public static void ropeLadder() {
        ItemStack ladder = new ItemStack(Material.LADDER, 2);
        ItemMeta meta = ladder.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Rope Ladder");
        ladder.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(ladder));
        recipe.addIngredient(Material.LADDER).addIngredient(Material.LADDER).addIngredient(Material.STRING);
        Bukkit.addRecipe(recipe);
    }
}
