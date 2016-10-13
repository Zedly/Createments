package zedly.createments;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Replaces expressions in chat messages with dank memes. One in 500 messages is affected
 * @author Dennis
 */
public class AprilWatcher implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent evt) {
        if (Storage.rnd.nextInt(500) == 0) {
            String message = evt.getMessage();
            message = message.replaceAll("(?i)nyan(pig|cat)?", "Nyanpassu");
            message = message.replace("now", "meow");
            message = message.replaceAll("(?i)pony", "otter");
            if (message.matches("(?i)[lk]([oe]+[lk]+)+([oe]+)?")) {
                message = "I found that to be quite humorous, very good show indeed.";
            }
            message = message.replaceAll("(^| )o?k($| )", "blergh");
            message = message.replace("okay", "blergh");
            message = message.replace("pvp", "D-D-D-D-D-D-D-DUEL");
            message = message.replace("\\bfuck\\b", "STOP - HAMMERTIME");
            message = message.replace("shit", "STOP - HAMMERTIME");

            message = message.replace("food", "cocaine");
            switch (message) {
                case "yes":
                    message = "no";
                    break;
                case "no":
                    message = "yes";
                    break;
            }
            message = message.replace("arrow", "anal penetration device");

            message = message.replace("thanks", "boobies");
            message = message.replace("thx", "boobies");

            message = message.replaceAll("\\bhi\\b", "ヽ༼ຈل͜ຈ༽ﾉ raise ur dongers ");
            message = message.replace("hello", "ヽ༼ຈل͜ຈ༽ﾉ raise ur dongers");
            message = message.replace("please", "based tyrone");
            message = message.replace("pls", "based tyrone");
            message = message.replace("sorry", "m8");
            message = message.replace("sry", "m8");
            message = message.replaceAll("(?i)om(f)?g", "OH BABY A TRIPLE");
            message = message.replace("help", "noodle");

            message = message.replace(":D", "(͜͡ʖ͡)");
            message = message.replace("xD", "(͜͡ʖ͡)");
            message = message.replace(":)", "(͜͡ʖ͡)");
            evt.setMessage(message);
        }
    }
}
