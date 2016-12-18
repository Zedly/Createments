package zedly.createments;

import zedly.createments.projectiles.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

public class Storage {

    public static final Random rnd = new Random();
    public static final String logo = ChatColor.COLOR_CHAR + "9[" + ChatColor.COLOR_CHAR + "3Createments" + ChatColor.COLOR_CHAR + "9]" + ChatColor.COLOR_CHAR + "b";
    public static final Map<Entity, AdvancedProjectile> advancedProjectiles = new HashMap<>();
    
    // Arrows mapped to different advanced arrow effects, to be used by the Arrow Watcher to perform these effects
    public static final Map<Entity, Set<AdvancedArrow>> advancedArrows = new HashMap<>();
    
    public static final LinkedHashMap<String, String> emoticonSubstitutions = new LinkedHashMap<>();
    public static final LinkedHashSet<Entity> fairies = new LinkedHashSet<>();
    
    // Lighting entities created by Lightning Arrows
    public static final List<Entity> lightnings = new ArrayList<>();
    
    // Entities an advanced arrow has damaged or killed
    public static final Map<Entity, AdvancedArrow> killedEntities = new HashMap<>();
    
    // Webs from the Web Arrow elemental arrow
    public static final Set<Block> webs = new HashSet<>();
    
    // Entities being affected by the Derp Elemental Arrow
    public static final Set<LivingEntity> derpingEntities = new HashSet<>();
    
    public static final int[] rainbowcolors = {0xFF0000, 0xFF8800, 0xFFFF00, 0x88FF00, 0x00FF00, 0x00FF88, 0x00FFFF, 0x0088FF, 0x0000FF, 0x8800FF, 0xFF00FF, 0xFF0088};
    public static final String[] textrainbow = {"§4", "§6", "§e", "§a", "§2", "§b", "§1", "§3", "§9", "§5", "§d", "§c"};
    public static Createments createments;
    public static final Set<Player> rainbowplayers = new HashSet<>();

    public static Map<Block, Boolean> decay = new HashMap<>();
    public static final char BOX_CHAR = 9632; // '■'
}
