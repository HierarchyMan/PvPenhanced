package cystol.pvp;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import cystol.pvp.Knockback;

public class PotOptimize implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8.8")) {
                float pitch = player.getLocation().getPitch();
                if (pitch > 30.0) {
                    if (itemInHand.getType() == Material.POTION) {

                        event.setCancelled(true);
                        // Create the thrown splash potion entity
                        ThrownPotion potion = player.launchProjectile(ThrownPotion.class);
                        potion.setItem(itemInHand);

                        // Set the position and velocity of the thrown potion
                        Vector direction = player.getEyeLocation().getDirection();
                        Vector spawnOffset = direction.multiply(0.85);
                        potion.teleport(player.getLocation().add(spawnOffset).add(0, 1, 0)); // Spawn 1 block above player
                        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                            player.getInventory().remove(itemInHand);
                        }
                        potion.setVelocity(direction.multiply(5)); // Set velocity in player's look direction
                    }
                }
            }
        }
        if (event.getAction().name().contains("RIGHT_CLICK") &&
                (!Bukkit.getVersion().contains("1.7") && !Bukkit.getVersion().contains("1.8"))) {
            float pitch = player.getLocation().getPitch();
            if (pitch > 30.0) {
                if (player.getItemInHand().getType() == Material.SPLASH_POTION || player.getItemInHand().getType() == Material.POTION) {

                    event.setCancelled(true);
                    // Create the thrown splash potion entity
                    ThrownPotion potion = player.launchProjectile(ThrownPotion.class);
                    potion.setItem(player.getItemInHand());

                    // Set the position and velocity of the thrown potion
                    Vector direction = player.getEyeLocation().getDirection();
                    Vector spawnOffset = direction.multiply(0.85);
                    potion.teleport(player.getLocation().add(spawnOffset).add(0, 1, 0)); // Spawn 1 block above player
                    player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
                    if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                        player.getInventory().remove(player.getItemInHand());
                    }
                    potion.setVelocity(direction.multiply(5)); // Set velocity in player's look direction

                }
            }
        }

    }
}