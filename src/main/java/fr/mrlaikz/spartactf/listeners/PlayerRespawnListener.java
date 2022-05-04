package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onInteract(PlayerRespawnEvent e) {
        Event event = SpartaCTF.getInstance().getEventManager().getEvent();
        if(event != null && event.getState().equals(EventState.PLAYING) && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            ((ListenerInterface) event).onPlayerRespawn(e);
        }
    }
}
