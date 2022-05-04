package fr.mrlaikz.spartactf.interfaces;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public interface ListenerInterface {

    void onPlayerMove(PlayerMoveEvent e);
    void onPlayerDeath(PlayerDeathEvent e);
    void onPlayerInteract(PlayerInteractEvent e);
    void onPlayerJoin(PlayerJoinEvent e);
    void onPlayerRespawn(PlayerRespawnEvent e);
    void onDamage(EntityDamageByEntityEvent e);
    void onPlayerLeave(PlayerQuitEvent e);

}
