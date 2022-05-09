package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(PlayerDeathEvent e) {

        if(SpartaCTF.getInstance().getEventManager().getEvent() != null && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            Event event = SpartaCTF.getInstance().getEventManager().getEvent();
            ((ListenerInterface) event).onPlayerDeath(e);
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamaged(EntityDamageByEntityEvent e) {
        if (SpartaCTF.getInstance().getEventManager().getEvent() != null && ((Player)e.getDamager()).getGameMode().equals(GameMode.ADVENTURE) && SpartaCTF.getInstance().getEventManager().getEvent().getState().equals(EventState.PLAYING)) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Event event = SpartaCTF.getInstance().getEventManager().getEvent();
                if (event != null) {
                    ((ListenerInterface) event).onDamage(e);
                }
            }
        }
    }

}
