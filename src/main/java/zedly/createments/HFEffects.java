package zedly.createments;

import java.util.HashSet;
import java.util.LinkedList;
import org.bukkit.*;
import static org.bukkit.Material.AIR;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

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
    }
}
