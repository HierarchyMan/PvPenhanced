package cystol;

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

public class playerkb implements Listener {
    private final JavaPlugin plugin;
    double knockbackHorizontal = 0.4D;
    double knockbackVerticalRaw = 0.69D;
    double knockbackVertical = 0.4D;
    double knockbackSprintHorizontal = 1D;
    double knockbackSprintVertical = 0.1D;
    double groundHorizontalMultiplier = 1.25D;
    double groundVerticalMultiplier = 1D;
    double frictionHorizontal = 1D;
    double frictionVertical = 1D;
    double speedHorizontal = 1D;
    double speedVert = 0.36D;
    double speedSprintMultiplier = 1D;
    double speedGroundHorMultiplier = 1D;
    double knockbackEnchantincreaseConstant = 1.25D;
    boolean airkbissprintkb;
    double comboMaxNoDamageTicks = 10;
    HashMap<Player, Vector> playerKnockbackHashMap = new HashMap<>();

    public playerkb(JavaPlugin plugin) {
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

            if (plugin.getConfig().getBoolean("airkbissprintkb")) {
                if (!victim.isOnGround()) {
                    attacker.setSprinting(true);

                }
            }


            double d0 = attacker.getLocation().getX() - victim.getLocation().getX();
            double d1;

            for (d1 = attacker.getLocation().getZ() - victim.getLocation().getZ();
                 d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
                d0 = (Math.random() - Math.random()) * 0.01D;

            double magnitude = Math.sqrt(d0 * d0 + d1 * d1);


            Vector playerVelocity = victim.getVelocity();


            if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                for (PotionEffect effect : attacker.getActivePotionEffects()) {
                    if (effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() > 0) {
                        playerVelocity.setX((playerVelocity.getX() / 2) - (d0 / magnitude * (speedHorizontal + 0.2)));
                        playerVelocity.setY((playerVelocity.getY() / 2) + knockbackVerticalRaw);
                        playerVelocity.setZ((playerVelocity.getZ() / 2) - (d1 / magnitude * (speedHorizontal + 0.2)));
                    }
                }
            } else {
                playerVelocity.setX((playerVelocity.getX() / 2) - (d0 / magnitude * (knockbackHorizontal + 0.2)));
                playerVelocity.setY((playerVelocity.getY() / 2) + knockbackVerticalRaw);
                playerVelocity.setZ((playerVelocity.getZ() / 2) - (d1 / magnitude * (knockbackHorizontal + 0.2)));
            }


            int i = attacker.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
            int knockbackLevel = attacker.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
            double enchantkb = 1.0 + ((knockbackEnchantincreaseConstant - 1) * knockbackLevel);

            if (playerVelocity.getY() > knockbackVertical) {
                if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                    for (PotionEffect effect : attacker.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() > 0) {
                            playerVelocity.setY(speedVert);
                        }
                    }
                } else {
                    playerVelocity.setY(knockbackVertical);
                }

            }

            if (i > 0)
                playerVelocity.multiply(new Vector(knockbackSprintHorizontal * enchantkb, knockbackSprintVertical, knockbackSprintHorizontal * enchantkb));

            if (frictionVertical != 1 || frictionHorizontal != 1) {
                playerVelocity.divide(new Vector(frictionHorizontal, frictionVertical, frictionHorizontal));
            }

            if (victim.isOnGround()) {
                if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                    for (PotionEffect effect : attacker.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() > 0) {

                            playerVelocity.multiply(new Vector(speedGroundHorMultiplier, 1, speedGroundHorMultiplier));

                        }
                    }
                } else {

                    playerVelocity.multiply(new Vector(groundHorizontalMultiplier, groundVerticalMultiplier, groundHorizontalMultiplier));
                }
            }
            if (attacker.isSprinting()) {
                if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                    for (PotionEffect effect : attacker.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() > 0) {

                            playerVelocity.multiply(new Vector(speedSprintMultiplier, 1, speedSprintMultiplier));

                        }
                    }
                } else {

                    playerVelocity.multiply(new Vector(knockbackSprintHorizontal, knockbackSprintVertical, knockbackSprintHorizontal));
                }
            }

            if (victim.getMaximumNoDamageTicks() > comboMaxNoDamageTicks) {
                playerKnockbackHashMap.put(victim, playerVelocity);
            }
        }
    }


    private void getConfigValues() {
        knockbackHorizontal = plugin.getConfig().getDouble("knockbackHorizontal");
        knockbackVerticalRaw = plugin.getConfig().getDouble("knockbackVerticalRaw");
        knockbackVertical = plugin.getConfig().getDouble("knockbackVertical");
        knockbackSprintHorizontal = plugin.getConfig().getDouble("knockbackSprintHorizontal");
        knockbackSprintVertical = plugin.getConfig().getDouble("knockbackSprintVertical");
        groundHorizontalMultiplier = plugin.getConfig().getDouble("groundHorizontalMultiplier");
        groundVerticalMultiplier = plugin.getConfig().getDouble("groundVerticalMultiplier");
        frictionHorizontal = plugin.getConfig().getDouble("frictionHorizontal");
        frictionVertical = plugin.getConfig().getDouble("frictionVertical");
        speedHorizontal = plugin.getConfig().getDouble("speedHorizontal");
        speedGroundHorMultiplier = plugin.getConfig().getDouble("speedGroundHorMultiplier");
        speedSprintMultiplier = plugin.getConfig().getDouble("speedSprintMultiplier");
        knockbackEnchantincreaseConstant = plugin.getConfig().getDouble("knockbackEnchantincreaseConstant");
        speedVert = plugin.getConfig().getDouble("speedVert");
        airkbissprintkb = plugin.getConfig().getBoolean("airkbissprintkb");
        comboMaxNoDamageTicks = plugin.getConfig().getDouble("combo.MaxNoDamageTicks");
    }
}