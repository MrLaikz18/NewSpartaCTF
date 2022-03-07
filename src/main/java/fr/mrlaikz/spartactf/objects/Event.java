package fr.mrlaikz.spartactf.objects;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.Color;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.enums.Status;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Event implements ListenerInterface {

    private Player owner;
    private Map map;
    private List<Team> teams;
    private EventState state;

    public Event(Player owner, Map map) {
        this.owner = owner;
        this.map = map;
        this.teams = new ArrayList<>();
        this.state = EventState.WAITING;
        teams.add(new Team(Color.BLUE));
        teams.add(new Team(Color.RED));
    }

    //GETTERS
    public Player getOwner() {
        return owner;
    }

    public Map getMap() {
        return map;
    }

    public Team getTeam(Color c) {
        if(c.equals(Color.BLUE)) {
            return teams.get(0);
        }
        if(c.equals(Color.RED)) {
            return teams.get(1);
        }
        return null;
    }

    public EventState getState() {
        return state;
    }

    //SETTERS
    public void setState(EventState state) {
        this.state = state;
    }

    //VOIDERS
    ///PLAYER MANAGEMENT
    public void joinPlayer(Player p) {
        if(getTeam(Color.BLUE).getMembers().size() > getTeam(Color.RED).getMembers().size()) {
            getTeam(Color.RED).addPlayer(p);
            p.sendMessage("§aVous avez rejoins l'équipe §c§lRouge");
        } else if (getTeam(Color.BLUE).getMembers().size() < getTeam(Color.RED).getMembers().size()) {
            getTeam(Color.BLUE).addPlayer(p);
            p.sendMessage("§aVous avez rejoins l'équipe §9§lBleue");
        } else {
            getTeam(Color.RED).addPlayer(p);
            p.sendMessage("§aVous avez rejoins l'équipe §c§lRouge");
        }
        p.teleport(map.getSpawnLocation());
    }

    ///FLAG MANAGEMENT
    public void spawnFlags() {
        for(Team t : teams) {
            Location loc = map.getFlagLocation(t.getColor());
            loc.getBlock().setType(t.getColor().getMaterial());
        }
    }

    public void captureFlag(Player capturer, Team captured) {
        capturer.getInventory().setHelmet(new ItemStack(captured.getFlag().getMaterial()));
        captured.getFlag().getLocation().getBlock().setType(Material.AIR);
        captured.getFlag().setStatus(Status.TAKEN);
        Bukkit.broadcastMessage("§aLe drapeau " + captured.getColor().getName() + " §aa été capturé !");
    }

    public void fallFlag(Player capturer, Team captured) {
        capturer.getLocation().getBlock().setType(capturer.getInventory().getHelmet().getType());
        capturer.getInventory().setHelmet(new ItemStack(Material.AIR));
        captured.getFlag().setStatus(Status.FREE);
        Bukkit.broadcastMessage("§aLe drapeau " + captured.getColor().getName() + " §aest maintenant libre !");
    }

    public void resetFlag(Team team) {
        team.getFlag().getLocation().getBlock().setType(Material.AIR);
        map.getFlagLocation(team.getColor()).getBlock().setType(team.getFlag().getMaterial());
        team.getFlag().setStatus(Status.FREE);
        Bukkit.broadcastMessage("§aLe drapeau " + team.getColor().getName() + " §aa été reset");
    }

    ///LISTENERS MANAGER
    @Override
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        Team playerTeam = getTeamFromPlayer(p);
        Team enemyTeam = getEnemyTeam(playerTeam);


        if(matchLocations(to, enemyTeam.getFlag().getLocation())) {
            captureFlag(p, enemyTeam);
        }

        if(matchLocations(to, map.getFlagLocation(playerTeam.getColor())) && p.getInventory().getHelmet().getType().equals(enemyTeam.getFlag().getMaterial())) {
            stop(playerTeam);
        }

        if(matchLocations(to, playerTeam.getFlag().getLocation()) && !matchLocations(playerTeam.getFlag().getLocation(), map.getFlagLocation(playerTeam.getColor()))) {
            resetFlag(playerTeam);
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Team playerTeam = getTeamFromPlayer(p);
        Team enemyTeam = getEnemyTeam(playerTeam);

        if(p.getInventory().getHelmet().getType().equals(enemyTeam.getFlag().getMaterial()) && enemyTeam.getFlag().getStatus().equals(Status.TAKEN)) {
            fallFlag(p, enemyTeam);
        }

    }

    ///EVENT MANAGEMENT
    public void start() {
        spawnFlags();
        this.state = EventState.PLAYING;
        //TODO TIMER
        for(Team t : teams) {
            for(Player p : t.getMembers()) {
                p.teleport(map.getFlagLocation(t.getColor()));
            }
        }
    }

    public void stop(Team winner) {
        this.state = null;
        SpartaCTF.getInstance().getEventManager().stopEvent(this);
    }

    //UTILS
    public boolean matchLocations(Location loc1, Location loc2) {
        int x1 = (int) loc1.getX();
        int y1 = (int) loc1.getY();
        int z1 = (int) loc1.getZ();

        int x2 = (int) loc2.getX();
        int y2 = (int) loc2.getY();
        int z2 = (int) loc2.getZ();

        return(x1 == x2 && y1 == y2 && z1 == z2);
    }

    public Team getTeamFromPlayer(Player p) {
        for(Team t : teams) {
            if(t.getMembers().contains(p)) {
                return t;
            }
        }
        return null;
    }

    public Team getEnemyTeam(Team t) {
        if(t.getColor().equals(Color.RED)) {
            return teams.get(0);
        } else if(t.getColor().equals(Color.BLUE)) {
            return teams.get(1);
        }
        return null;
    }



}
