package zedly.createments;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

/**
 * Creates a storm of lightning and fireballs for about one night
 * @author Dennis
 */
public class Apocalypse implements Runnable {

    Random rnd;
    int id;
    int ticks = 0;
    private final World world;
    private static final HashMap<World, Apocalypse> runningApocalypses = new HashMap<>();

    public static boolean isApocalypseRunning(World world) {
        return runningApocalypses.containsKey(world);
    }

    public static void startApocalypse(World world) {
        if (runningApocalypses.containsKey(world)) {
            return;
        }
        Apocalypse ap = new Apocalypse(world);
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storage.createments, ap, 1, 1);
        ap.setId(id);
        runningApocalypses.put(world, ap);
    }

    public static void stopApocalypse(World world) {
        if (!runningApocalypses.containsKey(world)) {
            return;
        }
        Bukkit.getScheduler().cancelTask(runningApocalypses.get(world).getId());
    }

    private Apocalypse(World world) {
        this.world = world;
        this.rnd = new Random();
    }

    private int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void run() {
        if (ticks > 2000) {
            Bukkit.getScheduler().cancelTask(id);
        }
        if (ticks == 1) {
            world.setStorm(true);
            world.setThundering(true);
        }
        if (ticks > 20) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location loc = player.getLocation();
                double x = loc.getX() + rnd.nextInt(100) - 50;
                double z = loc.getZ() + rnd.nextInt(100) - 50;
                if (((int) x) % 4 == 0) {
                    player.getWorld().strikeLightningEffect(player.getWorld().getHighestBlockAt(new Location(loc.getWorld(), x, 0, z)).getLocation());
                }
                if (ticks > 400) {
                    double y = loc.getY() + rnd.nextInt(50) + 100;
                    Entity ent = player.getWorld().spawnEntity(new Location(loc.getWorld(), x, y, z), EntityType.FIREBALL);
                    Fireball fb = (Fireball) ent;
                    fb.setDirection(new Vector(-1, -10, -1));
                    fb.setIsIncendiary(false);
                }
            }
        }
        ticks++;
    }
}
