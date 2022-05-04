package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(SpartaCTF.getInstance().getEventManager().getEvent() != null && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE) && SpartaCTF.getInstance().getEventManager().getEvent().getState().equals(EventState.WAITING)) {
            Event event = SpartaCTF.getInstance().getEventManager().getEvent();
            ((ListenerInterface) event).onPlayerJoin(e);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (SpartaCTF.getInstance().getEventManager().getEvent() != null && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE) && SpartaCTF.getInstance().getEventManager().getEvent().getState().equals(EventState.PLAYING)) {
            Event event = SpartaCTF.getInstance().getEventManager().getEvent();
            ((ListenerInterface) event).onPlayerLeave(e);
        }
    }

}
