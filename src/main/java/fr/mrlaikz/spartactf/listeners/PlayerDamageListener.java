package fr.mrlaikz.spartactf.listeners;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.objects.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(PlayerDeathEvent e) {

        if(SpartaCTF.getInstance().getEventManager().getEvent() != null) {
            Event event = SpartaCTF.getInstance().getEventManager().getEvent();
            ((ListenerInterface) event).onPlayerDeath(e);
        }

    }

}
