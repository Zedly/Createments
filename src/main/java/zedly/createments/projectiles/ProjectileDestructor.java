package zedly.createments.projectiles;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.SmallFireball;
import zedly.createments.Storage;
import zedly.fireworkeffects.FireworkEffectPlayer;

public class ProjectileDestructor extends AdvancedProjectile {

    public static final FireworkEffect.Builder bu;

    public ProjectileDestructor(SmallFireball sf) {
        super(sf);
    }

    public void trail() {
        try {
            Storage.fep.playFirework(sf.getLocation(), bu.build());
        } catch (Exception ex) {
            sf.remove();
        }
    }
    
    public void impact() {
        sf.getLocation().getWorld().createExplosion(sf.getLocation(), 5);
    }

    static {
        bu = FireworkEffect.builder().withColor(org.bukkit.Color.fromRGB(0x000055)).trail(true).with(FireworkEffect.Type.BALL);
    }
}
