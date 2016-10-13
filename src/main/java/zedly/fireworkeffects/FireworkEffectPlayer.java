package zedly.fireworkeffects;

import java.util.List;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class FireworkEffectPlayer {

    private final JavaPlugin hostPlugin;
    private ServerVersion serverVersion = ServerVersion.UNKNOWN;

    public FireworkEffectPlayer(JavaPlugin hostPlugin) {
        this.hostPlugin = hostPlugin;
        try {
            Class.forName("net.minecraft.server.v1_10_R1.EntityFireworks");
            serverVersion = ServerVersion.NMS_1_10_R1;
        } catch (Exception e) {
        }
        System.out.println("Detected NMS Version: " + serverVersion);
    }

    public void playFirework(Location loc, org.bukkit.FireworkEffect.Type type, org.bukkit.Color color, org.bukkit.Color fade, boolean trail, boolean twinkle) {
        FireworkEffect.Builder bu = FireworkEffect.builder();
        bu.withColor(color);
        bu.withFade(fade);
        bu.with(type);
        if (trail) {
            bu.withTrail();
        }
        if (twinkle) {
            bu.withFlicker();
        }
        playFirework(loc, bu.build());
    }

    public void playFirework(Location loc, org.bukkit.FireworkEffect.Type type, org.bukkit.Color color, boolean trail, boolean twinkle) {
        FireworkEffect.Builder bu = FireworkEffect.builder();
        bu.withColor(color);
        bu.with(type);
        if (trail) {
            bu.withTrail();
        }
        if (twinkle) {
            bu.withFlicker();
        }
        playFirework(loc, bu.build());
    }

    public void playFirework(Location loc, org.bukkit.FireworkEffect.Type type, List<org.bukkit.Color> color, boolean trail, boolean twinkle) {
        FireworkEffect.Builder bu = FireworkEffect.builder();
        bu.withColor(color);
        bu.with(type);
        if (trail) {
            bu.withTrail();
        }
        if (twinkle) {
            bu.withFlicker();
        }
        playFirework(loc, bu.build());
    }

    public void playFirework(Location loc, org.bukkit.FireworkEffect.Type type, List<org.bukkit.Color> color, List<org.bukkit.Color> fade, boolean trail, boolean twinkle) {
        FireworkEffect.Builder bu = FireworkEffect.builder();
        bu.withColor(color);
        bu.withFade(fade);
        bu.with(type);
        if (trail) {
            bu.withTrail();
        }
        if (twinkle) {
            bu.withFlicker();
        }
        playFirework(loc, bu.build());
    }

    public void playFirework(Location location, FireworkEffect effect) {
        CustomEntityFirework firework;
        switch (serverVersion) {
            case NMS_1_10_R1:
                firework = new CustomEntityFirework_1_10_R1(location, effect);
                break;
            default:
                firework = new CustomEntityFirework_Unknown(hostPlugin, location, effect);
        }
        firework.perform();
    }

    private enum ServerVersion {
        NMS_1_10_R1,
        UNKNOWN
    }
}
