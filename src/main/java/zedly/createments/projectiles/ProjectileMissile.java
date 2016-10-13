package zedly.createments.projectiles;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SmallFireball;
import zedly.fireworkeffects.FireworkEffectPlayer;
import zedly.createments.Storage;

public class ProjectileMissile extends AdvancedProjectile {

    public static final FireworkEffect.Builder bu;

    public ProjectileMissile(SmallFireball sf) {
        super(sf);
    }

    @Override
    public void trail() {
        try {
            Storage.fep.playFirework(sf.getLocation(), bu.build());
        } catch (Exception ex) {
            sf.remove();
        }
    }

    @Override
    public void impact() {
        sf.getLocation().getWorld().createExplosion(sf.getLocation().getX(), sf.getLocation().getY(), sf.getLocation().getZ(), 5, false, false);
    }

    static {
        bu = FireworkEffect.builder().withColor(org.bukkit.Color.fromRGB(Storage.rainbowcolors[1])).trail(true).with(FireworkEffect.Type.BALL);
    }
}
