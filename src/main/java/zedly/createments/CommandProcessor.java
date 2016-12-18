package zedly.createments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.*;
import static org.bukkit.Material.ARROW;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import zedly.createments.projectiles.ElementalArrow;

public class CommandProcessor {

    public static void apocalypse(CommandSender sender, String worldName) {
        if (!sender.hasPermission("createments.apocalypse")) {
            return;
        }
        World world;
        if (!worldName.isEmpty()) {
            world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage(Storage.logo + " invalid world!");
                return;
            }
        } else if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else {
            sender.sendMessage(Storage.logo + " please specify a world!");
            return;
        }
        if (!Apocalypse.isApocalypseRunning(world)) {
            Apocalypse.startApocalypse(world);
            sender.sendMessage(Storage.logo + " Apocalype has begun!");
        } else {
            sender.sendMessage(Storage.logo + " Apocalype has been cancelled..");
        }
    }

    public static void aura(CommandSender sender) {
        if (!sender.hasPermission("createments.aura")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        final Player player = (Player) sender;
        if (Aura.isEnabledFor(player)) {
            Aura.disableFor(player);
            player.sendMessage(Storage.logo + " Aura disabled!");
        } else {
            Aura.enableFor(player);
            player.sendMessage(Storage.logo + " Aura enabled!");
        }
    }

    public static void blocks(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        Player player = (Player) sender;
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[Server] OH MY GOD WHAT HAVE YOU DONE");
        Location loc = player.getLocation();
        for (int dx = -10; dx < 10; dx++) {
            for (int dy = -10; dy < 10; dy++) {
                for (int dz = -10; dz < 10; dz++) {
                    if (player.getWorld().getBlockAt(loc.getBlockX() + dx, loc.getBlockY() + dy, loc.getBlockZ() + dz).getType() != Material.AIR) {
                        Location loc1 = new Location(player.getWorld(), loc.getBlockX() + dx, loc.getBlockY() + dy, loc.getBlockZ() + dz);
                        try {
                            player.sendBlockChange(loc1, Material.values()[Storage.rnd.nextInt(200)], (byte) Storage.rnd.nextInt(16));
                        } catch (IllegalArgumentException ex) {
                        }
                    }
                }
            }
        }
    }

    public static void emotes(CommandSender sender) {
        if (!sender.hasPermission("createments.emoticons")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        boolean comma = false;
        for (String replace : Storage.emoticonSubstitutions.keySet()) {
            if (comma) {
                sb.append(", ");
            }
            sb.append(replace);
            comma = true;
        }
        sender.sendMessage(Storage.logo + "Available emoticons:");
        sender.sendMessage(sb.toString());
    }

    public static void fusrodah(CommandSender sender) {
        if (!sender.hasPermission("createments.fusrodah")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        Player player = (Player) sender;
        for (Entity ent : player.getNearbyEntities(5, 5, 5)) {
            Vector dv = ent.getLocation().subtract(player.getLocation()).toVector();
            dv.multiply(1 / dv.length());
            ent.setVelocity(dv.add(new Vector(0, 1.5, 0)));
        }
        player.getWorld().createExplosion(player.getLocation(), 0);
    }

    public static void rainboom(CommandSender sender) {
        if (!sender.hasPermission("createments.rainboom")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        Player player = (Player) sender;
        if (Rainboom.isEnabledFor(player)) {
            Rainboom.disableFor(player);
            player.sendMessage(Storage.logo + " Rainboom disabled!");
        } else {
            Rainboom.enableFor(player);
            player.sendMessage(Storage.logo + " Rainboom enabled!");
        }
    }

    public static void paragrapher(CommandSender sender) {
        if (!sender.hasPermission("createments.paragrapher")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " You must be a Player to spawn a Paragrapher!");
            return;
        }
        Player player = (Player) sender;
        ItemStack stk = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bm = (BookMeta) stk.getItemMeta();
        bm.addPage("Equation for X:\n");
        bm.addPage("Equation for Y:\n");
        bm.addPage("Equation for Z:\n");
        bm.addPage("t Min:\n");
        bm.addPage("t Max:\n");
        bm.addPage("t Increment:\n");
        bm.addPage("t Delay:\n");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Paragrapher");
        bm.setLore(lore);
        stk.setItemMeta(bm);
        player.getInventory().addItem(stk);
        player.sendMessage(Storage.logo + " Spawned a Paragrapher!");
    }

    public static void rainbow(CommandSender sender) {
        if (!sender.hasPermission("createments.rainbow")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        Player player = (Player) sender;
        synchronized (Storage.rainbowplayers) {
            if (Storage.rainbowplayers.contains(player)) {
                Storage.rainbowplayers.remove(player);
                sender.sendMessage(Storage.logo + " Rainbow mode disabled!");
            } else {
                Storage.rainbowplayers.add(player);
                sender.sendMessage(Storage.logo + " Rainbow mode enabled!");
            }
        }
    }

    public static void reload(CommandSender sender) {
        if (!sender.hasPermission("createments.reload")) {
            sender.sendMessage(Storage.logo + " You do not have permission to do this!");
        }
        try {
            Createments.getPlugin(Createments.class).loadConfig();
            sender.sendMessage(Storage.logo + " Configuration reloaded!");
        } catch (Exception ex) {
            sender.sendMessage(Storage.logo + ChatColor.RED + " Reload failed! Createments will be unable to start with this config");
        }
    }

    // Lists all Elemental Arrows 
    public static void arrowList(CommandSender sender) {
        if (!sender.hasPermission("createments.arrow.info")) {
            sender.sendMessage(Storage.logo + "You do not have permission to do this!");
            return;
        }
        sender.sendMessage(Storage.logo + "Arrow Types:");
        for (String str : ElementalArrow.arrows.keySet()) {
            sender.sendMessage(ChatColor.DARK_AQUA + "- " + ChatColor.AQUA + str);
        }
    }

    // Gives information on the Elemental Arrow on the held arrowCraft or on the one named in the parameter
    public static void arrowInfo(CommandSender sender, String[] args, ItemStack stack) {
        if (!sender.hasPermission("createments.arrow.info")) {
            sender.sendMessage(Storage.logo + "You do not have permission to do this!");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return;
        }
        if (args.length >= 3) {
            for (ElementalArrow ar : ElementalArrow.arrows.values()) {
                if (ar.getName().toLowerCase().startsWith(args[2])) {
                    sender.sendMessage(Storage.logo + "Arrow Info:");
                    sender.sendMessage(ChatColor.DARK_AQUA + "- " + ar.getName() + ": " + ChatColor.AQUA + ar.getDescription());
                    return;
                }
            }
            sender.sendMessage(Storage.logo + "Could not find the type of Arrow you're looking for!");
        } else {
            if (stack.getType() == ARROW) {
                if (stack.getItemMeta().hasLore()) {
                    String str = stack.getItemMeta().getLore().get(0);
                    str = ChatColor.stripColor(str);
                    for (String string : ElementalArrow.arrows.keySet()) {
                        if (string.equals(str)) {
                            sender.sendMessage(Storage.logo + "Arrow Info:");
                            sender.sendMessage(ChatColor.DARK_AQUA + "- " + str + ": " + ChatColor.AQUA + ElementalArrow.arrows.get(str).getDescription());
                        }
                    }
                }
            }
        }
    }

    // Lists all the commands associated with Elemental Arrows
    public static boolean arrowHelp(CommandSender player, String label) {
        if (label.equals("") || label.equals("help")) {
            player.sendMessage(ChatColor.BLUE + "[" + ChatColor.DARK_AQUA + "Zenchantments" + ChatColor.BLUE + "] ");
            player.sendMessage(ChatColor.DARK_AQUA + "- " + "arrow info: " + ChatColor.AQUA + "Returns information about custom arrows.");
            player.sendMessage(ChatColor.DARK_AQUA + "- " + "arrow list: " + ChatColor.AQUA + "Returns a list of custom arrows");
            player.sendMessage(ChatColor.DARK_AQUA + "- " + "arrow <arrow type> <?arguments> <?arguments>: " + ChatColor.AQUA + "Adds the desired arrow effect to the arrow in hand.");
            return true;
        }
        return false;
    }

    // Turns the held arrowCraft into an Elemental Arrow determined by the parameters
    public static boolean arrowCraft(CommandSender sender, String[] args, String label, ItemStack stack) {
        if (!sender.hasPermission("zenchantments.arrow.create")) {
            sender.sendMessage(Storage.logo + "You do not have permission to do this!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Storage.logo + " This only works ingame!");
            return true;
        }
        Player player = (Player) sender;
        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() != ARROW) {
            sender.sendMessage(Storage.logo + "You need to be holding arrows for this command!");
            return true;
        }
        for (ElementalArrow ar : ElementalArrow.arrows.values()) {
            if (ar.getName().toLowerCase().startsWith(label)) {
                List<String> lore = ar.constructArrow(Arrays.copyOfRange(args, 2, args.length));
                if (lore == null) {
                    player.sendMessage(Storage.logo + ar.getCommand());
                    return true;
                }
                ItemMeta soMeta = stack.getItemMeta();
                soMeta.setLore(lore);
                stack.setItemMeta(soMeta);
                player.getInventory().setItemInMainHand(stack);
                sender.sendMessage(Storage.logo + "Created " + ar.getName() + "s!");
                return true;
            }
        }
        sender.sendMessage(Storage.logo + "That arrow does not exist!");
        return true;
    }

    // Control flow for the command processor
    public static boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
        ItemStack stack = null;
        if (sender instanceof Player) {
            stack = ((Player) sender).getInventory().getItemInMainHand();
        }
        String label = "";
        if (!(args.length == 0)) {
            label = args[0].toLowerCase().replace("_", "");
        }
        String cmd = commandlabel.toLowerCase();
        switch (cmd) {
            case "cr":
                switch (label) {
                    case "reload":
                        reload(sender);
                        break;
                    case "rainbow":
                        rainbow(sender);
                        break;
                    case "rainboom":
                        rainboom(sender);
                        break;
                    case "paragrapher":
                        paragrapher(sender);
                        break;
                    case "fusrodah":
                        fusrodah(sender);
                        break;
                    case "emotes":
                        emotes(sender);
                        break;
                    case "blocks":
                        blocks(sender);
                        break;
                    case "aura":
                        aura(sender);
                        break;
                    case "apocalypse":
                        apocalypse(sender, args.length >= 2 ? args[1] : "");
                        break;
                    case "arrow":
                        if (args.length > 1) {
                            switch (args[1]) {
                                case "list": {

                                    arrowList(sender);
                                    break;
                                }
                                case "info": {
                                    arrowInfo(sender, args, stack);
                                    break;
                                }
                                case "help":
                                default:
                                    return arrowHelp(sender, args[1]) ? true : arrowCraft(sender, args, args[1], stack);
                            }
                        }
                }
        }
        return true;
    }

}
