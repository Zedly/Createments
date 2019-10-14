package zedly.createments;

import java.util.LinkedList;
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
    }
}
