package cystol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class talipearls implements Listener {

    private final JavaPlugin plugin;

    public talipearls(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPearlLand(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl) || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        final EnderPearl enderPearl = (EnderPearl) event.getEntity();
        final Location enderLocation = enderPearl.getLocation().clone();
        final Player player = (Player) enderPearl.getShooter();

        if (enderPearl.hasMetadata("tali") && this.getLocTali(enderLocation, player) != null) {
            player.teleport(this.getLocTali(enderLocation, player));
        } else {
            final Block block = this.getBlockTali(enderLocation, player);
            if (block == null) {
            }
            // Additional logic for handling the block
        }
    }

    private String getDirectionName(final Location location) {
        double rotation = (location.getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }

        if (0.0 <= rotation && rotation < 22.5) {
            return "W";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "W";
        }
        return null;
    }

    private Block getBlockTali(final Location location, final Player player) {
        final String directionName = this.getDirectionName(location);
        Location newLoc = null;
        switch (directionName) {
            case "N":
            case "NW": {
                newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "W":
            case "SW": {
                newLoc = new Location(player.getWorld(), player.getLocation().getX() - 0.7, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "S":
            case "SE": {
                newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "E":
            case "NE": {
                newLoc = new Location(player.getWorld(), player.getLocation().getX() + 0.7, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            default: {
                return null;
            }
        }
        return (newLoc == null) ? null : newLoc.getBlock();
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        final Location location = event.getTo();
        if (location.getBlock() == null || location.getBlock().getType() == Material.AIR) {
            return;
        }

        final String directionName = this.getDirectionName(location);
        switch (directionName) {
            case "N":
            case "NW":
            case "W":
            case "SW": {
                location.setX(location.getX() + 0.55);
                location.setZ(location.getZ() + 0.55);
                break;
            }
            default: {
                location.setX(location.getX() - 0.55);
                location.setZ(location.getZ() - 0.55);
                break;
            }
        }
        event.setTo(location);
    }

    private Location getLocTali(final Location location, final Player player) {
        final String directionName = this.getDirectionName(player.getLocation());
        Location newLoc = null;
        switch (directionName) {
            case "N":
            case "NW": {
                newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() - 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "W":
            case "SW": {
                newLoc = new Location(player.getWorld(), location.getX() - 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "S":
            case "SE": {
                newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() + 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            case "E":
            case "NE": {
                newLoc = new Location(player.getWorld(), location.getX() + 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
            default: {
                return null;
            }
        }
        return newLoc;
    }
}
