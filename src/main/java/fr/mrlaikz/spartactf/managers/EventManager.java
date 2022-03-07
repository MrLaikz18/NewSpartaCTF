package fr.mrlaikz.spartactf.managers;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EventManager {

    private SpartaCTF plugin;

    public EventManager(SpartaCTF plugin) {
        this.plugin = plugin;
    }

    private Event event;

    //GETTERS
    public Event getEvent() {
        return event;
    }

    //SETTERS
    public void loadEvent(Event e) {
        event = e;
        for(String s : plugin.getConfig().getStringList("broadcast.event_prepare")) {
            String colored = ChatColor.translateAlternateColorCodes('&', s);
            //BROADCAST COLORED
        }
    }

    public void stopEvent(Event e) {
        event = null;
        //TODO
        //FINIR STOP
        //TIMERS
        //GESTION MANAGER - COMMANDS
        //RECOMPENSES
        //BROADCAST BUNGEE
        //(GUI GESTION EVENT)
    }

}
