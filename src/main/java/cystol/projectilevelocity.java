package cystol;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class projectilevelocity implements Listener {

    private final JavaPlugin plugin;

    public projectilevelocity(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent event) {
        Player player = (Player) event.getEntity().getShooter();
        Entity entity = event.getEntity();
        if (entity instanceof FishHook) {
            Vector velo = entity.getVelocity();
            Vector velo2 = velo.multiply(plugin.getConfig().getDouble("rod.speedMultipler"));
            entity.setVelocity(velo2);
        }
        if (entity instanceof EnderPearl) {
            Vector velo = entity.getVelocity();
            Vector velo2 = velo.multiply(plugin.getConfig().getDouble("pearl.speedMultipler"));
            entity.setVelocity(velo2);
        }
        if (entity instanceof ThrownPotion) {
            Vector velo = entity.getVelocity();
            Vector velo2 = new Vector(velo.multiply(plugin.getConfig().getDouble("PotionThrowSpeedMultiplier")).getX(), velo.getY(), velo.multiply(plugin.getConfig().getDouble("PotionThrowSpeedMultiplier")).getZ());
            entity.setVelocity(velo2);
            World world = entity.getWorld();
            Location location = entity.getLocation();
            Vector direction = player.getEyeLocation().getDirection();
            double potOffsetY = plugin.getConfig().getDouble("potoffsetY");
            double dispX = plugin.getConfig().getDouble("potoffsetX");

            // Adjust the location based on the configured offsets
            double newX = location.getX() + (direction.getX() * dispX);
            double newY = location.getY() + potOffsetY;
            double newZ = location.getZ() + (direction.getZ() * dispX);

            Location newLocation = new Location(world, newX, newY, newZ);
            entity.teleport(newLocation);
        }
    }
}
