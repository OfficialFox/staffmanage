package nl.officialfox.staffmanage;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeTracker {
    private Map<Player, Instant> joinTimes = new HashMap<>();

    public void playerJoined(Player player) {
        joinTimes.put(player, Instant.now());
    }

    public String getPlaytime(Player player) {
        Instant joinTime = joinTimes.get(player);
        if (joinTime == null) return "0m";

        Duration playtime = Duration.between(joinTime, Instant.now());

        long minutes = playtime.toMinutes();
        return String.format("%dm", minutes);
    }

    public void playerLeft(Player player) {
        joinTimes.remove(player);
    }

    public String getRank(Player player) {

        if (player.hasPermission("staff.helper")) {
            return "staff.helper";
        }

        if (player.hasPermission("staff.mod")) {
            return "staff.mod";
        }

        if (player.hasPermission("staff.admin")) {
            return "staff.admin";
        }

        return "";

    }
    private Map<UUID, Integer> weeklyPlaytime = new HashMap<>();

    public int getWeeklyPlaytime(Player player) {
        Integer weekly = weeklyPlaytime.get(player.getUniqueId());
        if (weekly == null){
            return 0;
        }
        return weekly;
    }

    //java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()" because the return value of "java.util.Map.get(Object)" is null
    public void resetWeeklyPlaytime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weeklyPlaytime.clear();
    }

    private int lastResetWeek = -1;

//    public void scheduleWeeklyReset() {
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("staffmanage"), () -> {
//            int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
//            if (currentWeek != lastResetWeek) {
//                weeklyPlaytime.clear();
//                lastResetWeek = currentWeek;
//            }
//        }, 0L, 20L);
//    }

}
