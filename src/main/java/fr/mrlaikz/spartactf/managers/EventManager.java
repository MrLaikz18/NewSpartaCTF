package fr.mrlaikz.spartactf.managers;

import fr.iban.common.teleport.EventAnnouce;
import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.menus.ConfigMenu;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EventManager {

    private SpartaCTF plugin;

    public EventManager(SpartaCTF plugin) {
        this.plugin = plugin;
    }

    private Event event;
    private ConfigMenu eventMenu;

    //GETTERS
    public Event getEvent() {
        return event;
    }

    //SETTERS
    public void setEvent(Event e) {
        this.event = e;
    }

    public void setEventState(EventState state) {
        event.setState(state);
    }

    public void stopEvent(Event e) {
        event = null;
        eventMenu = null;
    }

    public void prepareMenu(ConfigMenu menu) {
        this.eventMenu = menu;
    }

    public ConfigMenu getEventMenu() {
        return this.eventMenu;
    }

}
