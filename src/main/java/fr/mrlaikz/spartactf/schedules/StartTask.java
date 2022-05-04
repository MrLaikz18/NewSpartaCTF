package fr.mrlaikz.spartactf.schedules;

import fr.mrlaikz.spartactf.objects.Event;
import fr.mrlaikz.spartactf.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static reactor.core.reactivestreams.PublisherFactory.forEach;

public class StartTask extends BukkitRunnable {

    private int timer = 10;
    private final Event event;

    public StartTask(Event event) {
        this.event = event;
    }

    @Override
    public void run() {

        if (timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
             for(Player p : event.getMap().getSpawnLocation().getWorld().getNearbyPlayers(event.getMap().getSpawnLocation(), 100)) {
                 p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1f, 1f);
                 p.sendMessage("§aL'event va commencer dans " + timer + " secondes !");
             }
        }

        if (timer == 0) {
            event.start();
            cancel();
        }

        timer--;
    }

}
