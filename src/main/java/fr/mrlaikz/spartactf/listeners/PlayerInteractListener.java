package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Event event = SpartaCTF.getInstance().getEventManager().getEvent();
        if(event != null && event.getState().equals(EventState.WAITING)) {
            ((ListenerInterface) event).onPlayerInteract(e);
        }
    }

}
