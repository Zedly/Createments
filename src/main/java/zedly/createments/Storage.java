package zedly.createments;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class Storage {

    public static final Random rnd = new Random();
    public static final String logo = ChatColor.COLOR_CHAR + "9[" + ChatColor.COLOR_CHAR + "3Createments" + ChatColor.COLOR_CHAR + "9]" + ChatColor.COLOR_CHAR + "b";
    
    
    public static final LinkedHashMap<String, String> emoticonSubstitutions = new LinkedHashMap<>();
    
    public static final int[] rainbowcolors = {0xFF0000, 0xFF8800, 0xFFFF00, 0x88FF00, 0x00FF00, 0x00FF88, 0x00FFFF, 0x0088FF, 0x0000FF, 0x8800FF, 0xFF00FF, 0xFF0088};
    public static final String[] textrainbow = {"§4", "§6", "§e", "§a", "§2", "§b", "§1", "§3", "§9", "§5", "§d", "§c"};
    public static Createments createments;
    public static final Set<Player> rainbowplayers = new HashSet<>();

    public static final char BOX_CHAR = 9632; // '■'
}
