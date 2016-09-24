package com.leontg77.teaminv.commands;

import com.google.common.collect.Lists;
import com.leontg77.teaminv.Main;
import com.leontg77.teaminv.listeners.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TeamInv command class.
 *
 * @author LeonTG77
 */
public class TeamInvCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "teaminv.manage";

    private final Main plugin;

    private final DeathListener listener;
    private final Scoreboard board;

    public TeamInvCommand(Main plugin, DeathListener listener) {
        this.listener = listener;
        this.plugin = plugin;

        this.board = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private boolean enabled = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (!enabled) {
                    sender.sendMessage(Main.PREFIX + "Usage: /teaminv <info|enable|disable>");
                    return true;
                }

                Player player = (Player) sender;
                Team team = board.getPlayerTeam(player);

                if (team == null) {
                    sender.sendMessage(Main.PREFIX + "Usage: /teaminv <info|enable|disable>");
                    return true;
                }

                if (!plugin.getTeamInvs().containsKey(team)) {
                    plugin.getTeamInvs().put(team, Bukkit.createInventory(player, 36, "§4Team inventory."));
                }

                player.openInventory(plugin.getTeamInvs().get(team));
            } else {
                sender.sendMessage(Main.PREFIX + "Usage: /teaminv <info|enable|disable>");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG77");
            sender.sendMessage(Main.PREFIX + "Version: §a" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (enabled) {
                sender.sendMessage(Main.PREFIX + "Team Inventory is already enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Team Inventory has been enabled.");
            enabled = true;

            Bukkit.getPluginManager().registerEvents(listener, plugin);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "Team Inventory is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Team Inventory has been disabled.");
            enabled = false;

            HandlerList.unregisterAll(listener);
            plugin.getTeamInvs().clear();
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /teaminv <info|enable|disable>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = Lists.newArrayList();
        List<String> list = Lists.newArrayList();

        if (args.length != 1) {
            return toReturn;
        }

        list.add("info");

        if (sender.hasPermission(PERMISSION)) {
            list.add("enable");
            list.add("disable");
        }

        // make sure to only tab complete what starts with what they
        // typed or everything if they didn't type anything
        toReturn.addAll(list
                .stream()
                .filter(str -> args[args.length - 1].isEmpty() || str.startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList()));

        return toReturn;
    }
}