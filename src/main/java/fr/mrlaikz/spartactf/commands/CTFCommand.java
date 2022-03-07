package fr.mrlaikz.spartactf.commands;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.objects.Event;
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

                if(args.length == 0) {
                    if(plugin.getEventManager().getEvent() != null && plugin.getEventManager().getEvent().getState().equals(EventState.WAITING)) {
                        Event event = plugin.getEventManager().getEvent();
                        event.joinPlayer(p);
                    } else {
                        p.sendMessage("§cVous ne pouvez pas rejoindre cet event actuellement !");
                    }
                }

                if(args.length == 2) {
                    if(p.hasPermission("spartacube.event.ctf")) {
                        if(args[0].equalsIgnoreCase("prepare")) {
                            String map = args[1].toLowerCase(Locale.ROOT);
                        }
                    }
                }

            } else {
                sender.sendMessage("IMPOSSIBLE DE FAIRE CELA");
            }

        }

        return false;
    }
}
