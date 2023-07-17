package cystol.pvp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class ComboKnockback implements Listener {

    private final Map<Player, Vector> playerVelocities = new HashMap<>();

    public static int getMaxNoDamageTicks(Player player) {
        return player.getMaximumNoDamageTicks();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (damaged == attacker) {
            return; // Don't perform any actions if a player damages themselves
        }
        int maxNoDamageTicks = ComboKnockback.getMaxNoDamageTicks(damaged);
        if (maxNoDamageTicks <= 6) {
            attacker.sendMessage("debug");
            cancelVelocityEvents(damaged);
            addKnockbackVelocity(attacker, damaged);
        }
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        int maxNoDamageTicks = ComboKnockback.getMaxNoDamageTicks(player.getPlayer());
        if (playerVelocities.containsKey(player) && maxNoDamageTicks <= 6) {
            event.setVelocity(playerVelocities.get(player));
            playerVelocities.remove(player);

        }
    }

    private void cancelVelocityEvents(Player player) {
        int maxNoDamageTicks = ComboKnockback.getMaxNoDamageTicks(player.getPlayer());
        if (maxNoDamageTicks <= 6) {
            playerVelocities.put(player, player.getVelocity());
        }
    }

    private void addKnockbackVelocity(Player attacker, Player damaged) {
        attacker.setSprinting(false);
        double comboLimit = 0.001D;
        double knockbackHorizontal = -0.45D; // Set your desired horizontal knockback value here
        double knockbackVertical = -0.55D; // Set your desired vertical knockback value here

        Vector direction = damaged.getLocation().subtract(attacker.getLocation()).toVector().normalize();
        Vector knockbackVelocity = direction.multiply(knockbackHorizontal).setY(knockbackVertical);

        if (damaged.getVelocity().getY() > comboLimit && damaged.getVelocity().getY() == comboLimit) {
            knockbackVelocity.setY(comboLimit);;
        }
        attacker.setSprinting(false);
        damaged.setVelocity(knockbackVelocity);

    }
}
