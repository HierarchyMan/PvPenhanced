package cystol;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class bowboost implements Listener {
    private final JavaPlugin plugin;

    public bowboost(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        Arrow arrow;
        Entity damager;
        Player p = e.getPlayer();
        Vector velocity = e.getVelocity();
        EntityDamageEvent event = p.getLastDamageCause();
        if (event != null &&
                !event.isCancelled() &&
                event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof Arrow &&
                (arrow = (Arrow) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("straightBowBoost")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = arrow.getLocation().getDirection().normalize();
            double xVelocity = plugin.getConfig().getDouble("bowboostHorizontal");
            double yVelocity = plugin.getConfig().getDouble("bowboostVertical");
            double zVelocity = xVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * xVelocity, velocity.getY() * yVelocity, dir.getZ() * speed * zVelocity);

            e.setVelocity(newVelocity);
        }
    }

    @EventHandler
    public void OnSelfArrowDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (event.getEntity() == player && !event.isCancelled()) {
                    event.setDamage(0);
                }
            }
        }

    }
}
