package fr.mrlaikz.spartactf.managers;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.objects.Event;

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

}
