package cystol;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class combokb implements Listener {
    private final JavaPlugin plugin;
    double ComboHorizontal = 0.4D;
    double ComboVertical = 0.4D;
    double ComboVerticalLimit = 0.4D;
    double comboMaxNoDamageTicks;
    double maxComboHeight;

    HashMap<Player, Vector> playerKnockbackHashMap = new HashMap<>();

    public combokb(JavaPlugin plugin) {
        this.plugin = plugin;
        getConfigValues();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
        if (!playerKnockbackHashMap.containsKey(event.getPlayer())) return;
        event.setVelocity(playerKnockbackHashMap.get(event.getPlayer()));
        playerKnockbackHashMap.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && !event.isCancelled() && event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {


            if (!(event.getEntity() instanceof Player)) return;
            Player victim = (Player) event.getEntity();

            if (!(event.getDamager() instanceof Player)) return;
            if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
            Player attacker = (Player) event.getDamager();


            double d0 = attacker.getLocation().getX() - victim.getLocation().getX();
            double d1;

            for (d1 = attacker.getLocation().getZ() - victim.getLocation().getZ();
                 d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
                d0 = (Math.random() - Math.random()) * 0.01D;

            double magnitude = Math.sqrt(d0 * d0 + d1 * d1);


            Vector playerVelocity = victim.getVelocity();


            playerVelocity.setX((playerVelocity.getX() / 2) - (d0 / magnitude * (ComboHorizontal + 0.2)));
            playerVelocity.setY((playerVelocity.getY() / 2) + ComboVertical);
            playerVelocity.setZ((playerVelocity.getZ() / 2) - (d1 / magnitude * (ComboHorizontal + 0.2)));


            if (playerVelocity.getY() > ComboVerticalLimit) {
                playerVelocity.setY(ComboVerticalLimit);
            }
            if (victim.getVelocity().getY() > maxComboHeight && victim.getMaximumNoDamageTicks() <= comboMaxNoDamageTicks) {
                victim.setVelocity(new Vector(victim.getVelocity().getX(), -1 * maxComboHeight, victim.getVelocity().getZ()));
            }


            if (victim.getMaximumNoDamageTicks() <= comboMaxNoDamageTicks) {
                playerKnockbackHashMap.put(victim, playerVelocity);
            }
        }
    }


    private void getConfigValues() {
        ComboHorizontal = plugin.getConfig().getDouble("combo.horizontal");
        ComboVertical = plugin.getConfig().getDouble("combo.vertical");
        ComboVerticalLimit = plugin.getConfig().getDouble("combo.v-limit");
        comboMaxNoDamageTicks = plugin.getConfig().getDouble("combo.MaxNoDamageTicks");
        maxComboHeight = plugin.getConfig().getDouble("combo.maxComboHeight");
    }
}