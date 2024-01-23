package cystol;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class kb extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        Plugin plugin = this;

        playerkb playerkbInstance = new playerkb(this);
        getServer().getPluginManager().registerEvents(playerkbInstance, this);

        bowboost bowboostInstance = new bowboost(this);
        getServer().getPluginManager().registerEvents(bowboostInstance, this);

        entity entityInstance = new entity(this);
        getServer().getPluginManager().registerEvents(entityInstance, this);

        projectilevelocity projectilevelocityI = new projectilevelocity(this);
        getServer().getPluginManager().registerEvents(projectilevelocityI, this);

        talipearls talipearlsI = new talipearls(this);
        getServer().getPluginManager().registerEvents(talipearlsI, this);

        combokb combokbi = new combokb(this);
        getServer().getPluginManager().registerEvents(combokbi, this);

        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultConfig();
        config = getConfig();

    }

    @EventHandler
    public void onFight(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            if (getConfig().getBoolean("OverrideMaxNoDamageTicks")) {
                int dmgtcks = getConfig().getInt("DamageTicks");
                damager.setMaximumNoDamageTicks(dmgtcks);
                player.setMaximumNoDamageTicks(dmgtcks);
            }


        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getConfig().getBoolean("OverrideMaxNoDamageTicks")) {
            int dmgtcks = getConfig().getInt("DamageTicks");
            player.setMaximumNoDamageTicks(dmgtcks);
        }
    }

    @Override
    public void onDisable() {

    }
}
