package nl.officialfox.staffmanage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Staffmanage extends JavaPlugin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (label.equals("gocraftstaff")) {
            try {
                openStaffUI(player);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return true;
        }
        return false;
    }

    private void openStaffUI(Player player) throws ParseException {

        Inventory gui = Bukkit.createInventory(player, 27, ChatColor.DARK_GREEN + "Staff UI");

        gui.setItem(11, createPlaytimeItem(player));
        gui.setItem(13, createOnlineStaffItem(getOnlineStaff()));

        player.openInventory(gui);
    }
    private PlaytimeTracker playtimeTracker = new PlaytimeTracker();

    private ItemStack createPlaytimeItem(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Jouw online tijd");
        List<String> lore = new ArrayList<>();
        String playtime = playtimeTracker.getPlaytime(player);
        lore.add(ChatColor.GRAY + playtime);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }


    List<Player> onlineStaff = new ArrayList<>();

    private List<Player> getOnlineStaff() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("staff")) {
                onlineStaff.removeAll(onlineStaff);
                onlineStaff.add(player);
            }
        }

        return onlineStaff;
    }
    private ItemStack createOnlineStaffItem(List<Player> onlineStaff) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Online Staff");
        List<String> lore = new ArrayList<>();

        for (Player staff : onlineStaff) {
            lore.add(ChatColor.GRAY + "- " + staff.getName());
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;

    }

    @Override
    public void onEnable() {
        StaffPlaytimeManager manager = new StaffPlaytimeManager();
        manager.checkRequiredPlaytime();
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getCommand("gocraftstaff").setExecutor(this);
    }
}