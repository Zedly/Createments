package zedly.createments.projectiles;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SmallFireball;
import zedly.fireworkeffects.FireworkEffectPlayer;
import zedly.createments.Storage;

public class ProjectileSignal extends AdvancedProjectile {

    public static FireworkEffect.Builder bu;

    public ProjectileSignal(SmallFireball sf) {
        super(sf);
        sf.setVelocity(sf.getVelocity().multiply(3));
    }

    @Override
    public void trail() {
        if(tick == 8) {
            impact();
        }
    }

    @Override
    public void impact() {
        try {
            Storage.fep.playFirework(sf.getLocation(), bu.build());
        } catch (Exception ex) {
            sf.remove();
        }
    }

    static {
        bu = FireworkEffect.builder().withColor(org.bukkit.Color.fromRGB(Storage.rainbowcolors[0])).trail(true).with(FireworkEffect.Type.BALL_LARGE);
    }
}
