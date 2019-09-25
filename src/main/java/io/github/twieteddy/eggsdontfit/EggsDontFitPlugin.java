package io.github.twieteddy.eggsdontfit;

import java.io.File;
import java.util.Objects;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;


@SuppressWarnings("WeakerAccess")
public class EggsDontFitPlugin extends JavaPlugin implements Listener {
  private String spawnerChangeDeniedMessage;

  @Override
  public void onEnable() {
    loadConfig();
    registerEvents();
  }

  private void loadConfig() {
    File configYml = new File(getDataFolder(), "config.yml");
    if (!configYml.exists())
      saveResource("config.yml", false);
    YamlConfiguration config = YamlConfiguration.loadConfiguration(configYml);
    spawnerChangeDeniedMessage = translateColor(config.getString("denymessage"));
  }

  private void registerEvents() {
    getServer().getPluginManager().registerEvents(this, this);
  }

  private String translateColor(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  @SuppressWarnings("unused")
  @EventHandler
  public void onPlayerInteractEvent(PlayerInteractEvent e) {
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK
        && Objects.requireNonNull(e.getClickedBlock()).getType() == Material.SPAWNER
        && e.getPlayer().hasPermission("eggsdontfit.denyspawnerchange")) {
      e.setCancelled(true);
      e.getPlayer().spigot().sendMessage(
          ChatMessageType.ACTION_BAR,
          new ComponentBuilder(spawnerChangeDeniedMessage)
              .color(ChatColor.RED)
              .create());
    }
  }
}
