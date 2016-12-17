package zedly.createments;

import java.util.ArrayList;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

public class CommandProcessor {

    public static boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
        switch (commandlabel.toLowerCase()) {
            case "cr":
                if (args.length != 0) {
                    switch (args[0]) {
                        case "apocalypse": {
                            if (!sender.hasPermission("createments.apocalypse")) {
                                break;
                            }
                            World world;
                            if (args.length == 2) {
                                world = Bukkit.getWorld(args[1]);
                                if (world == null) {
                                    sender.sendMessage(Storage.logo + " invalid world!");
                                    break;
                                }
                            } else if (sender instanceof Player) {
                                world = ((Player) sender).getWorld();
                            } else {
                                sender.sendMessage(Storage.logo + " please specify a world!");
                                break;
                            }
                            if (!Apocalypse.isApocalypseRunning(world)) {
                                Apocalypse.startApocalypse(world);
                                sender.sendMessage(Storage.logo + " Apocalype has begun!");
                            } else {
                                sender.sendMessage(Storage.logo + " Apocalype has been cancelled..");
                            }
                            break;
                        }
                        case "aura": {
                            if (!sender.hasPermission("createments.aura")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
                            }
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Storage.logo + " This only works ingame!");
                                return true;
                            }
                            final Player player = (Player) sender;
                            if (Aura.isEnabledFor(player)) {
                                Aura.disableFor(player);
                                player.sendMessage(Storage.logo + " Aura disabled!");
                            } else {
                                Aura.enableFor(player);
                                player.sendMessage(Storage.logo + " Aura enabled!");
                            }
                            break;
                        }
                        case "blocks": {
                            if (!(sender instanceof Player)) {
                                return true;
                            }
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Storage.logo + " This only works ingame!");
                                return true;
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
                            break;
                        }
                        case "emotes": {
                            if (!sender.hasPermission("createments.emoticons")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
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
                            break;
                        }
                        /*
                        case "fairy": {
                            if (!(sender instanceof Player)) {
                                return true;
                            }
                            Player player = (Player) sender;
                            Location loc = player.getTargetBlock(((HashSet<Material>) null), 50).getLocation();
                            World world = loc.getWorld();
                            Bat bat = (Bat) world.spawnEntity(loc, EntityType.BAT);
                            ExperienceOrb xpEnt = (ExperienceOrb) world.spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                            bat.setAI(false);
                            xpEnt.setGravity(false);
                            //bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                            bat.setPassenger(xpEnt);
                            Storage.fairies.add(xpEnt);
                            player.sendMessage("Fairy created");
                            break;
                        }
*/
                        case "fusrodah": {
                            if (!sender.hasPermission("createments.fusrodah")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
                            }
                            Player player = (Player) sender;
                            for (Entity ent : player.getNearbyEntities(5, 5, 5)) {
                                Vector dv = ent.getLocation().subtract(player.getLocation()).toVector();
                                dv.multiply(1 / dv.length());
                                ent.setVelocity(dv.add(new Vector(0, 1.5, 0)));
                            }
                            player.getWorld().createExplosion(player.getLocation(), 0);
                            break;
                        }
                        case "paragrapher": {
                            if (!sender.hasPermission("createments.paragrapher")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                break;
                            }
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Storage.logo + " You must be a Player to spawn a Paragrapher!");
                                return true;
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
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.YELLOW + "Paragrapher");
                            bm.setLore(lore);
                            stk.setItemMeta(bm);
                            player.getInventory().addItem(stk);
                            player.sendMessage(Storage.logo + " Spawned a Paragrapher!");
                            break;
                        }
                        case "rainboom": {
                            if (!sender.hasPermission("createments.rainboom")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
                            }
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Storage.logo + " This only works ingame!");
                                return true;
                            }
                            Player player = (Player) sender;
                            if (Rainboom.isEnabledFor(player)) {
                                Rainboom.disableFor(player);
                                player.sendMessage(Storage.logo + " Rainboom disabled!");
                            } else {
                                Rainboom.enableFor(player);
                                player.sendMessage(Storage.logo + " Rainboom enabled!");
                            }
                            break;
                        }
                        case "rainbow": {
                            if (!sender.hasPermission("createments.rainbow")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
                            }
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Storage.logo + " This only works ingame!");
                                return true;
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
                            break;
                        }
                        case "reload":
                            if (!sender.hasPermission("createments.rainbow")) {
                                sender.sendMessage(Storage.logo + " You do not have permission to do this!");
                                return true;
                            }
                            try {
                                Createments.getPlugin(Createments.class).loadConfig();
                                sender.sendMessage(Storage.logo + " Configuration reloaded!");
                            } catch (Exception ex) {
                                sender.sendMessage(Storage.logo + ChatColor.RED + " Reload failed! Createments will be unable to start with this config");
                            }
                            break;
                    }
                    break;
                }
        }
        return true;
    }
}
