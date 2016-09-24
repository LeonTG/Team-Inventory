package com.leontg77.teaminv.listeners;

import com.leontg77.teaminv.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Death listener class.
 *
 * @author LeonTG77
 */
public class DeathListener implements Listener {
    private final Scoreboard board;
    private final Main plugin;

    public DeathListener(Main plugin) {
        this.plugin = plugin;
        this.board = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Team team = board.getPlayerTeam(player);

        if (team == null) {
            return;
        }

        if (team.getSize() == 1 && plugin.getTeamInvs().containsKey(team)) {
            Inventory inv = plugin.getTeamInvs().get(team);

            for (ItemStack item : inv.getContents()) {
                if (item == null) {
                    continue;
                }

                if (item.getType() == Material.AIR) {
                    return;
                }

                event.getDrops().add(item);
            }

            plugin.getTeamInvs().remove(team);
        }

        team.removePlayer(player);
    }
}