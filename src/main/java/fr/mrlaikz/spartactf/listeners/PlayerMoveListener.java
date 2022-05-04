package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if(SpartaCTF.getInstance().getEventManager().getEvent() != null && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            Event event = SpartaCTF.getInstance().getEventManager().getEvent();
            ((ListenerInterface) event).onPlayerMove(e);
        }
    }

}
