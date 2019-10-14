package zedly.createments;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CommandProcessor {

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
                            Material mat;
                            do {
                                mat = Material.values()[Storage.rnd.nextInt(200)];
                            } while (!mat.isBlock());
                            player.sendBlockChange(loc1, mat.createBlockData());
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

    // Control flow for the command processor
    public static boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
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
                    case "emotes":
                        emotes(sender);
                        break;
                    case "blocks":
                        blocks(sender);
                        break;
                    case "aura":
                        aura(sender);
                        break;
                }
        }
        return true;
    }

}
