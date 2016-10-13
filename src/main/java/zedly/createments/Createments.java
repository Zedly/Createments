package zedly.createments;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import zedly.fireworkeffects.*;

public class Createments extends JavaPlugin {

    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();
        loadConfig();
        Storage.createments = this;
        Storage.fep = new FireworkEffectPlayer(this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HFEffects(), 0, 1);
        getServer().getPluginManager().registerEvents(new Watcher(), this);
        getServer().getPluginManager().registerEvents(new DeathCharmWatcher(), this);
        getServer().getPluginManager().registerEvents(new RagequitWatcher(), this);
        getServer().getPluginManager().registerEvents(new AprilWatcher(), this);
        Recipes.advancedDeathCharm();
        Recipes.deathCharm();
    }

    public boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
        return CommandProcessor.onCommand(sender, command, commandlabel, args);
    }

    private void loadConfig() {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        List<Map<?, ?>> emoticonList = fc.getMapList("replacements");
        for (Map<?, ?> map : emoticonList) {
            if (map.containsKey("replace") && map.containsKey("with")) {
                Object replace = map.get("replace");
                Object with = map.get("with");
                if (replace instanceof String && with instanceof String) {
                    Storage.emoticonSubstitutions.put((String) replace, (String) with);
                }
            }

        }
    }
}
