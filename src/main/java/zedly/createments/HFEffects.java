package zedly.createments;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.bukkit.*;
import static org.bukkit.Material.AIR;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import zedly.createments.projectiles.AdvancedArrow;

public class HFEffects implements Runnable {

    private final LinkedList<Entity> toRemove = new LinkedList<>();

    public void run() {
        Storage.advancedProjectiles.forEach((ent, pro) -> {
            if (pro.isAlive()) {
                pro.trail();
                pro.incrementTick();
            } else {
                toRemove.add(ent);
            }
        });

        for (Entity ent : toRemove) {
            Storage.advancedProjectiles.remove(ent);
        }

        HashSet<Block> del = new HashSet<>();
        for (Block blk : Storage.decay.keySet()) {
            if (Storage.decay.get(blk)) {
                for (int i = 256; i > blk.getY(); i--) {
                    Location l = blk.getLocation().clone();
                    l.setY(i);
                    if (l.getBlock().getType() == Material.SIGN_POST) {
                        l.getBlock().setType(AIR);
                        del.add(l.getBlock());
                    }
                }
                blk.setType(AIR);
                del.add(blk);
            }
        }
        for (Block b : del) {
            Storage.decay.remove(b);
        }
        
        for(Entity ent : Storage.fairies) {
            //ent.getWorld().spawnParticle(Particle.END_ROD, ent.getLocation(), 1, 0.05, 0.05, 0.05, 0);
            //ent.getWorld().spawnParticle(Particle.HEART, ent.getLocation(), 1, 0.25, 0.25, 0.25, 0);
        }
        elementalArrows();
    }
    
    // Repeated actions for certain elemental arrows
    private void elementalArrows() {
        // Remove arrows if they don't exist or if it's been longer than 30 seconds
        Iterator it = Storage.advancedProjectiles.values().iterator();
        while (it.hasNext()) {
            for (AdvancedArrow a : (Set<AdvancedArrow>) it.next()) {
                a.onFlight();
                a.tick();
                if (a.getArrow().isDead() || a.getTick() > 600) {
                    it.remove();
                    a.getArrow().remove();
                    break;
                }
            }
        }
        // Remove Webs from Web Arrows
        it = Storage.webs.iterator();
        while (it.hasNext()) {
            Block block = (Block) it.next();
            if (Storage.rnd.nextInt(175) == 0 && block.getChunk().isLoaded()) {
                block.setType(AIR);
                it.remove();
            }
        }
        // Move around derping entities from Derp Arrows
        for (LivingEntity ent : Storage.derpingEntities) {
            Location loc = ent.getLocation();
            loc.setYaw(Storage.rnd.nextFloat() * 360F);
            loc.setPitch(Storage.rnd.nextFloat() * 180F - 90F);
            ent.teleport(loc);
        }
    }
}
