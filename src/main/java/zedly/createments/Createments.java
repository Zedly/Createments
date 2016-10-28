package zedly.createments;

import java.util.List;
import java.util.Map;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
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

    public boolean loadConfig() {
        reloadConfig();
        FileConfiguration fc = getConfig();
        List<Map<?, ?>> emoticonList = fc.getMapList("replacements");
        Storage.emoticonSubstitutions.clear();
        for (Map<?, ?> map : emoticonList) {
            if (map.containsKey("replace") && map.containsKey("with")) {
                Object replace = map.get("replace");
                Object with = map.get("with");
                if (replace instanceof String && with instanceof String) {
                    Storage.emoticonSubstitutions.put((String) replace, (String) with);
                }
            }
        }
        return true;
    }
}
