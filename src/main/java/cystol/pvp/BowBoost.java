package cystol.pvp;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import cystol.pvp.Knockback;

public class BowBoost implements Listener {

    public double KNOCKBACK_X;
    public double KNOCKBACK_Y;
    public double KNOCKBACK_Z;

    public static BowBoost create(JavaPlugin plugin) {
        BowBoost bowBoost = new BowBoost();
        bowBoost.loadConfig(plugin.getConfig());
        return bowBoost;
    }

    public void loadConfig(FileConfiguration config) {
        KNOCKBACK_X = config.getDouble("bowboost-horizontal", 1.2D);
        KNOCKBACK_Y = config.getDouble("bowboost-vertical", 1.0D);
        KNOCKBACK_Z = KNOCKBACK_X;
    }



    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        Vector dir = entity.getLocation().getDirection();
        Arrow a = (Arrow) e.getProjectile();
        double speed = a.getVelocity().length();
        Vector vel = dir.multiply(speed);
        a.setVelocity(vel);
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        Arrow arrow;
        Entity damager;
        Player p = e.getPlayer();
        Vector velocity = e.getVelocity();
        EntityDamageEvent event = p.getLastDamageCause();
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent && (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof Arrow && (arrow = (Arrow) damager).getShooter().equals(p)) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = arrow.getLocation().getDirection().normalize();
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * KNOCKBACK_X, velocity.getY() * KNOCKBACK_Y, dir.getZ() * speed * KNOCKBACK_Z);

            e.setVelocity(newVelocity);
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Player entity = (Player) event.getEntity();
            Player damager = (Player) ((Arrow) event.getDamager()).getShooter();
            if (entity == damager) {
                event.setDamage(0);
            }
        }
    }
}
