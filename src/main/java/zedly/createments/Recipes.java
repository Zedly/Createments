package zedly.createments;

import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class Recipes {
    
    public static void apply() {
        killCounter();
    }
    
    public static void killCounter() {
        ItemStack paper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = paper.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Kill Counter");
        lore.add(ChatColor.GREEN + "Add to a tool:");
        lore.add(ChatColor.GREEN + " 1. Kill Counter in off-hand");
        lore.add(ChatColor.GREEN + " 2. Tool in main hand");
        lore.add(ChatColor.GREEN + " 3. Shift-right-click");
        meta.setLore(lore);
        paper.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(Storage.createments, "killcounter"), new ItemStack(paper));
        recipe.addIngredient(Material.PAPER).addIngredient(Material.IRON_INGOT).addIngredient(Material.REDSTONE);
        Bukkit.addRecipe(recipe);
    }
}
