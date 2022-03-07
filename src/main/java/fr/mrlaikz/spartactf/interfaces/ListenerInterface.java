package fr.mrlaikz.spartactf.interfaces;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public interface ListenerInterface {

    void onPlayerMove(PlayerMoveEvent e);
    void onPlayerDeath(PlayerDeathEvent e);

}
