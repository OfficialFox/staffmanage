package nl.officialfox.staffmanage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StaffPlaytimeManager {
    private Map<String, Integer> requiredPlaytime = new HashMap<>();
    private PlaytimeTracker playtimeTracker = new PlaytimeTracker();

    public StaffPlaytimeManager() {
        requiredPlaytime.put("staff.helper", 4);
        requiredPlaytime.put("staff.mod", (int) 4.5);
        requiredPlaytime.put("staff.admin", 5);

        playtimeTracker = new PlaytimeTracker();

    }

    public void checkRequiredPlaytime() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String rank = playtimeTracker.getRank(player);
            int required = requiredPlaytime.get(rank);
           int weeklyPlaytime = playtimeTracker.getWeeklyPlaytime(player);
            if (weeklyPlaytime < required) {
                punish(player);
            } else {
                reward(player);
            }
        }
        playtimeTracker.resetWeeklyPlaytime();
    }
    public void punish(Player player){
        player.sendMessage(ChatColor.DARK_RED + "GET DEMOTED :D");
    }

    public void reward(Player player){
        player.sendMessage(ChatColor.GREEN + "You have met the required playtime!");;
    }

}