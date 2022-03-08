package fr.mrlaikz.spartactf.commands;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.menus.ConfigMenu;
import fr.mrlaikz.spartactf.objects.Event;
import fr.mrlaikz.spartactf.objects.Map;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CTFCommand implements CommandExecutor {

    private SpartaCTF plugin;

    public CTFCommand(SpartaCTF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("ctf")) {

            if(sender instanceof Player) {

                Player p = (Player) sender;

                if(args.length == 1 && args[0].equalsIgnoreCase("reload") && p.hasPermission("spartacube.event.ctf")) {
                    plugin.reloadConfig();
                    p.sendMessage("§aConfiguration reloaded");
                }

                if(args.length == 2) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if(args[0].equalsIgnoreCase("prepare")) {
                            String str_map = args[1].toLowerCase(Locale.ROOT);
                            Map map = new Map(str_map);
                            p.teleport(map.getSpawnLocation());
                            Event e = new Event(p, map);
                            ConfigMenu menu = new ConfigMenu(p, plugin, plugin.getEventManager().getEvent());
                            menu.open();
                            plugin.getEventManager().setEvent(e);
                        }

                        if(args[0].equalsIgnoreCase("start")) {
                            plugin.getEventManager().getEvent().start();
                        }
                    }
                }

                if(args.length == 3) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if(args[0].equalsIgnoreCase("params")) {
                            String map = args[2].toLowerCase(Locale.ROOT);
                            Location loc = p.getLocation();
                            if(args[1].equalsIgnoreCase("spawn_location")) {
                                plugin.getConfig().set("maps." + map + ".spawn_location", loc);
                            } else if(args[1].equalsIgnoreCase("red_location")) {
                                plugin.getConfig().set("maps." + map + ".red_location", loc);
                            } else if(args[1].equalsIgnoreCase("blue_location")) {
                                plugin.getConfig().set("maps." + map + ".blue_location", loc);
                            } else {
                                p.sendMessage("§cParamètre inconnu");
                                return false;
                            }
                            plugin.saveConfig();
                            p.sendMessage("§aParamètre " + args[1] + " actualisé");
                        }
                    } else {
                        p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                    }
                }

            } else {
                sender.sendMessage("IMPOSSIBLE DE FAIRE CELA");
            }

        }

        return false;
    }
}
