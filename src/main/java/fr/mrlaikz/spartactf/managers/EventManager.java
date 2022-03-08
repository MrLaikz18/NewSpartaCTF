package fr.mrlaikz.spartactf.managers;

import fr.iban.common.teleport.EventAnnouce;
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
    public void setEvent(Event e) {
        this.event = e;
    }

    public void stopEvent(Event e) {
        event = null;
    }

}
