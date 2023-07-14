package cystol.pvp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import cystol.pvp.Knockback;


public class DamageTicks implements Listener {
    public DamageTicks(FileConfiguration config) {
        loadConfig(config);
        enableDamageTicks = config.getBoolean("enable-damage-ticks", false);
    }
    private int maxNoDamageTicks;
    private boolean enableDamageTicks;


    private void loadConfig(FileConfiguration config) {
        maxNoDamageTicks = config.getInt("max_no_damage_ticks", 3);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (enableDamageTicks) {
        Player player = event.getPlayer();
        player.setMaximumNoDamageTicks(maxNoDamageTicks);
    }}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (enableDamageTicks && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.setMaximumNoDamageTicks(maxNoDamageTicks);
        }
    }
}
