package fr.mrlaikz.spartactf.commands;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.menus.ConfigMenu;
import fr.mrlaikz.spartactf.objects.Event;
import fr.mrlaikz.spartactf.objects.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

                if(args.length == 0 && p.hasPermission("spartacube.event.ctf")) {
                    p.sendMessage("§c§l/ctf reload: §cReload de la configuration du plugin");
                    p.sendMessage("§c§l/ctf menu: §cOuverture du menu de l'event");
                    p.sendMessage("§c§l/ctf prepare <arene>: §cPréparation de l'event");
                    p.sendMessage("§c§l/ctf stuff <blue/red>: §cÉcriture du stuff de l'event");
                    p.sendMessage("§c§l/ctf params <args> <nom>: §cÉcriture d'une map de l'event");
                }

                if(args.length == 1) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            plugin.reloadConfig();
                            p.sendMessage("§aConfiguration reloaded");
                        } else if(args[0].equalsIgnoreCase("menu")) {
                            if(plugin.getEventManager().getEvent() != null) {
                                Event e = plugin.getEventManager().getEvent();
                                plugin.getEventManager().getEventMenu().open();
                            } else {
                                p.sendMessage("§cIl n'y pas d'event créé ");
                            }
                        } else {
                            p.sendMessage("§cSyntaxe: /ctf preparemenu <arene>");
                            p.sendMessage("§cSyntaxe: /ctf menu");
                        }
                    } else {
                        p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                    }

                }

                if(args.length == 2) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if(args[0].equalsIgnoreCase("prepare")) {
                            String str_map = args[1].toLowerCase(Locale.ROOT);
                            Map map = new Map(str_map);
                            p.teleport(map.getSpawnLocation());
                            Event e = new Event(p, map);
                            ConfigMenu menu = new ConfigMenu(p, plugin, e);
                            plugin.getEventManager().prepareMenu(menu);
                            menu.open();
                            plugin.getEventManager().setEvent(e);
                            plugin.getEventManager().setEventState(EventState.WAITING);
                        } else if(args[0].equalsIgnoreCase("stuff")) {
                            String color = args[1];

                            ItemStack chestplate = p.getInventory().getChestplate();
                            ItemStack leggings = p.getInventory().getLeggings();
                            ItemStack boots = p.getInventory().getBoots();

                            plugin.getConfig().set("stuff." + color + ".armor.chestplate", chestplate);
                            plugin.getConfig().set("stuff." + color + ".armor.leggings", leggings);
                            plugin.getConfig().set("stuff." + color + ".armor.boots", boots);

                            for(int i = 0; i<8; i++) {
                                plugin.getConfig().set("stuff." + color + ".stuff." + i, p.getInventory().getContents()[i]);
                            }

                            plugin.saveConfig();
                            p.sendMessage("§cStuff sauvegardé");

                        } else {
                            p.sendMessage("§cSyntaxe: /ctf prepare <arene>");
                            p.sendMessage("§cSyntaxe: /ctf menu");
                            p.sendMessage("§cSyntaxe: /ctf stuff <blue/red>");
                        }
                    } else {
                        p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                    }
                }

                if(args.length == 3) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if (args[0].equalsIgnoreCase("params")) {
                            String map = args[2].toLowerCase(Locale.ROOT);
                            Location loc = p.getLocation();
                            if (args[1].equalsIgnoreCase("spawn_location")) {
                                plugin.getConfig().set("maps." + map + ".spawn_location", loc);
                            } else if (args[1].equalsIgnoreCase("red_location")) {
                                plugin.getConfig().set("maps." + map + ".red_location", loc);
                            } else if (args[1].equalsIgnoreCase("blue_location")) {
                                plugin.getConfig().set("maps." + map + ".blue_location", loc);
                            } else {
                                p.sendMessage("§cParamètre inconnu");
                                return false;
                            }
                            plugin.saveConfig();
                            p.sendMessage("§aParamètre " + args[1] + " actualisé");
                        } else {
                            p.sendMessage("§cSyntaxe: /ctf params <args> <nom>");
                            p.sendMessage("§cargs: spawn_location / red_location / blue_location");
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
