package zedly.createments;

import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class Recipes {
    
    public static void apply() {
        cobweb();
        killCounter();
    }
    
    public static void cobweb() {
        ItemStack cobweb = new ItemStack(Material.COBWEB, 1);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Storage.createments, "cobweb"), cobweb);
        recipe.shape("SAS", "ASA", "SAS");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('A', Material.AIR);
        Bukkit.addRecipe(recipe);
    }
    
    public static void killCounter() {
        ItemStack paper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = paper.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Kill Counter");
        meta.setLore(lore);
        paper.setItemMeta(meta);
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(Storage.createments, "killcounter"), new ItemStack(paper));
        recipe.addIngredient(Material.PAPER).addIngredient(Material.IRON_INGOT).addIngredient(Material.REDSTONE);
        Bukkit.addRecipe(recipe);
    }
}
