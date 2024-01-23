package cystol;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class entity implements Listener {

    private final JavaPlugin plugin;

    public entity(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        Snowball snowball;
        Arrow arrow;
        FishHook rod;
        EnderPearl pearl;
        Fireball fireball;
        TNTPrimed tnt;
        Egg egg;
        Entity damager;
        Player p = e.getPlayer();
        Vector velocity = e.getVelocity();
        EntityDamageEvent event = p.getLastDamageCause();
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof Snowball &&
                !(snowball = (Snowball) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("snowball.override")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = snowball.getLocation().getDirection().normalize();
            double sbylimit = plugin.getConfig().getDouble("snowball.vertical");

            double sbxVelocity = plugin.getConfig().getDouble("snowball.horizontal");
            double sbyVelocity = 2D;
            double sbzVelocity = sbxVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, velocity.getY() * sbyVelocity, dir.getZ() * speed * sbxVelocity * 3.57);
            Vector newVelocity1 = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, sbylimit, dir.getZ() * speed * sbxVelocity * 3.57);

            e.setVelocity(newVelocity);
            e.setVelocity(newVelocity1);

        }
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof Egg &&
                !(egg = (Egg) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("egg.override")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = egg.getLocation().getDirection().normalize();
            double sbylimit = plugin.getConfig().getDouble("egg.vertical");

            double sbxVelocity = plugin.getConfig().getDouble("egg.horizontal");
            double sbyVelocity = 2D;
            double sbzVelocity = sbxVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, velocity.getY() * sbyVelocity, dir.getZ() * speed * sbxVelocity * 3.57);
            Vector newVelocity1 = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, sbylimit, dir.getZ() * speed * sbxVelocity * 3.57);

            e.setVelocity(newVelocity);
            e.setVelocity(newVelocity1);

        }
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof Arrow &&
                !(arrow = (Arrow) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("arrow.override")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = arrow.getLocation().getDirection().normalize();
            double sbylimit = plugin.getConfig().getDouble("arrow.vertical");

            double sbxVelocity = plugin.getConfig().getDouble("arrow.horizontal");
            double sbyVelocity = 2D;
            double sbzVelocity = sbxVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, velocity.getY() * sbyVelocity, dir.getZ() * speed * sbxVelocity * 3.57);
            Vector newVelocity1 = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, sbylimit, dir.getZ() * speed * sbxVelocity * 3.57);

            e.setVelocity(newVelocity);
            e.setVelocity(newVelocity1);

        }
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof EnderPearl &&
                !(pearl = (EnderPearl) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("pearl.override")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = pearl.getLocation().getDirection().normalize();
            double sbylimit = plugin.getConfig().getDouble("pearl.vertical");

            double sbxVelocity = plugin.getConfig().getDouble("pearl.horizontal");
            double sbyVelocity = 2D;
            double sbzVelocity = sbxVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, velocity.getY() * sbyVelocity, dir.getZ() * speed * sbxVelocity * 3.57);
            Vector newVelocity1 = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, sbylimit, dir.getZ() * speed * sbxVelocity * 3.57);

            e.setVelocity(newVelocity);
            e.setVelocity(newVelocity1);

        }
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent &&
                (damager = ((EntityDamageByEntityEvent) event).getDamager()) instanceof FishHook &&
                !(rod = (FishHook) damager).getShooter().equals(p) &&
                plugin.getConfig().getBoolean("rod.override")) {
            double speed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
            Vector dir = rod.getLocation().getDirection().normalize();
            double sbylimit = plugin.getConfig().getDouble("rod.vertical");

            double sbxVelocity = plugin.getConfig().getDouble("rod.horizontal");
            double sbyVelocity = 2D;
            double sbzVelocity = sbxVelocity;
            Vector newVelocity = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, velocity.getY() * sbyVelocity, dir.getZ() * speed * sbxVelocity * 3.57);
            Vector newVelocity1 = new Vector((dir.getX() * speed * -1.0) * sbxVelocity * 3.57, sbylimit, dir.getZ() * speed * sbxVelocity * 3.57);

            e.setVelocity(newVelocity);
            e.setVelocity(newVelocity1);

        }

    }
}
