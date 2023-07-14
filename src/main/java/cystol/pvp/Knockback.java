package cystol.pvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import cystol.pvp.DamageTicks;

import java.util.HashMap;

public class Knockback extends JavaPlugin implements Listener, CommandExecutor {
    double knockbackHorizontal = 0.4D;
    double knockbackVertical = 0.4D;
    double knockbackVerticalLimit = 0.4D;
    double knockbackExtraHorizontal = 0.5D;
    double knockbackExtraVertical = 0.1D;
    double knockbackSpeedMultiplier = 1.25;
    double kbspeedsprintmultiplier = 1.25;
    boolean netheriteKnockbackResistance;
    boolean versionHasNetherite = true;
    boolean hasShields = false;
    private boolean fixBowBoosting;
    private DamageTicks noDamageTicksPlugin;
    HashMap<Player, Vector> playerKnockbackHashMap = new HashMap<>();


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
        if (!playerKnockbackHashMap.containsKey(event.getPlayer())) return;
        event.setVelocity(playerKnockbackHashMap.get(event.getPlayer()));
        playerKnockbackHashMap.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        // Check if sword PvP, not PvE or EvE
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && !event.isCancelled() && event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if (hasShields && event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) != 0) {
                return;
            }

            if (!(event.getEntity() instanceof Player)) return;
            Player victim = (Player) event.getEntity();

            // Disable netherite kb, the knockback resistance attribute makes the velocity event not be called
            // Also it makes players sometimes just not take any knockback, and reduces knockback
            // This affects both PvP and PvE, so put it above the PvP check
            // We technically don't have to check the version but bad server jars might break if we do
            if (versionHasNetherite && !netheriteKnockbackResistance)
                for (AttributeModifier modifier : victim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getModifiers())
                    victim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).removeModifier(modifier);

            if (!(event.getDamager() instanceof Player)) return;
            if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
            if (event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) != 0) return;

            Player attacker = (Player) event.getDamager();

            // Figure out base knockback direction
            double d0 = attacker.getLocation().getX() - victim.getLocation().getX();
            double d1;

            for (d1 = attacker.getLocation().getZ() - victim.getLocation().getZ();
                 d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
                d0 = (Math.random() - Math.random()) * 0.01D;

            double magnitude = Math.sqrt(d0 * d0 + d1 * d1);

            // Get player knockback taken before any friction applied
            Vector playerVelocity = victim.getVelocity();
            // Apply friction then add the base knockback
            playerVelocity.setX((playerVelocity.getX() / 2) - (d0 / magnitude * knockbackHorizontal));
            playerVelocity.setY((playerVelocity.getY() / 2) + knockbackVertical);
            playerVelocity.setZ((playerVelocity.getZ() / 2) - (d1 / magnitude * knockbackHorizontal));

            // Calculate bonus knockback for sprinting or knockback enchantment levels
            int i = attacker.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
            if (attacker.isSprinting()) ++i;

            if (playerVelocity.getY() > knockbackVerticalLimit)
                playerVelocity.setY(knockbackVerticalLimit);

            // Apply bonus knockback
            if (i > 0)
                playerVelocity.add(new Vector((-Math.sin(attacker.getLocation().getYaw() * 3.1415927F / 180.0F) *
                        (float) i * knockbackExtraHorizontal), knockbackExtraVertical,
                        Math.cos(attacker.getLocation().getYaw() * 3.1415927F / 180.0F) *
                                (float) i * knockbackExtraHorizontal));


            // Allow netherite to affect the horizontal knockback
            if (netheriteKnockbackResistance) {
                double resistance = 1 - victim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue();
                playerVelocity.multiply(new Vector(resistance, 1, resistance));
            }

            // Knockback is sent immediately in 1.8+, there is no reason to send packets manually
            playerKnockbackHashMap.put(victim, playerVelocity);

            // Apply additional knockback if attacker has speed
            if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                double knockbackSpeedMultiplier = getConfig().getDouble("knockbackSpeedMultiplier");
                playerVelocity.multiply(new Vector(knockbackHorizontal * knockbackSpeedMultiplier, 1, knockbackHorizontal * knockbackSpeedMultiplier));
                if (attacker.isSprinting() && attacker.hasPotionEffect(PotionEffectType.SPEED) && kbspeedsprintmultiplier != 1.0)
                    playerVelocity.multiply(new Vector(knockbackHorizontal * knockbackSpeedMultiplier * kbspeedsprintmultiplier, 1, knockbackHorizontal * knockbackSpeedMultiplier * kbspeedsprintmultiplier));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            getConfigValues();
            sender.sendMessage(ChatColor.RED + "You have reloaded the config");
            return true;
        }

        return false;
    }

    private void getConfigValues() {
        FileConfiguration config = getConfig();
        knockbackHorizontal = config.getDouble("knockbackHorizontal");
        knockbackVertical = config.getDouble("knockbackVertical");
        knockbackVerticalLimit = config.getDouble("knockbackVerticalLimit");
        knockbackExtraHorizontal = config.getDouble("knockbackExtraHorizontal");
        knockbackExtraVertical = config.getDouble("knockbackExtraVertical");
        knockbackSpeedMultiplier = config.getDouble("knockbackSpeedMultiplier");
        kbspeedsprintmultiplier = config.getDouble("kbspeedsprintmultiplier");
        netheriteKnockbackResistance = config.getBoolean("enable-knockback-resistance", false) && versionHasNetherite;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        getConfigValues();


        // Hack around issue with knockback in wrong tick
        Bukkit.getScheduler().runTaskTimer(this, playerKnockbackHashMap::clear, 1, 1);
        if (getConfig().getBoolean("enable-damage-ticks", false)) {
            noDamageTicksPlugin = new DamageTicks(getConfig());
            getServer().getPluginManager().registerEvents(noDamageTicksPlugin, this);
        }
        if (getConfig().getBoolean("bow-boosting-fix", false)) {
            getServer().getPluginManager().registerEvents(new BowBoost(), this);
        }

        // haha this is terrible
        if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8") ||
                Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") ||
                Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12") ||
                Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15"))
            versionHasNetherite = false;

        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7")) return;
        hasShields = true;


    }
}
