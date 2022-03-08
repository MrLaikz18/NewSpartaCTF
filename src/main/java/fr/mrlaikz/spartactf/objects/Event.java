package fr.mrlaikz.spartactf.objects;

import fr.iban.bukkitcore.rewards.Reward;
import fr.iban.bukkitcore.rewards.RewardsDAO;
import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.Color;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.enums.Status;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import fr.mrlaikz.spartactf.menus.KitMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Event implements ListenerInterface {

    private Player owner;
    private Map map;
    private List<Team> teams;
    private EventState state;
    private Reward rewardWin;
    private Reward rewardParticip;

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

    public List<Team> getTeams() {
        return teams;
    }

    public EventState getState() {
        return state;
    }

    public Reward getRewardWin() {
        return rewardWin;
    }

    public Reward getRewardParticip() {
        return rewardParticip;
    }

    //SETTERS
    public void setState(EventState state) {
        this.state = state;
    }

    public void setRewardWin(Reward r) {
        rewardWin = r;
    }

    public void setRewardParticip(Reward r) {
        rewardParticip = r;
    }

    //VOIDERS
    ///PLAYER MANAGEMENT
    public void joinPlayer(Player p, Team team) {
        team.addPlayer(p);
        KitMenu menu = new KitMenu(p, SpartaCTF.getInstance());
        menu.open();
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
            stop(playerTeam, enemyTeam);
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

    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
            Team red = getTeam(Color.RED);
            Team blue = getTeam(Color.BLUE);

            if(p.getInventory().getItemInMainHand().getType().equals(Material.RED_WOOL)) {
                if(red.getMembers().size() <= blue.getMembers().size()) {
                    joinPlayer(p, red);
                } else {
                    p.sendMessage("§4Impossible de rejoindre l'équipe !");
                }

            } else if(p.getInventory().getItemInMainHand().getType().equals(Material.BLUE_WOOL)) {
                if(red.getMembers().size() >= blue.getMembers().size()) {
                    joinPlayer(p, blue);
                } else {
                    p.sendMessage("§4Impossible de rejoindre l'équipe !");
                }
            }

        }
    }

    ///EVENT MANAGEMENT
    public void start() {
        spawnFlags();

        Location loc = map.getSpawnLocation();
        Collection<Player> all = loc.getWorld().getNearbyPlayers(loc, 200);

        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta redM = red.getItemMeta();
        redM.setDisplayName("§cRejoindre l'équipe §c§lRouge");
        red.setItemMeta(redM);

        ItemStack blue = new ItemStack(Material.BLUE_WOOL);
        ItemMeta blueM = blue.getItemMeta();
        blueM.setDisplayName("§9Rejoindre l'équipe §9§lBleue");
        blue.setItemMeta(redM);

        for(Player p : all) {
            p.getInventory().clear();
            p.getInventory().addItem(red);
            p.getInventory().addItem(blue);
        }

        this.state = EventState.PLAYING;
        for(Team t : teams) {
            for(Player p : t.getMembers()) {
                p.teleport(map.getFlagLocation(t.getColor()));
            }
        }
    }

    public void stop(Team winner, Team looser) {
        this.state = null;
        SpartaCTF.getInstance().getEventManager().stopEvent(this);

        for(Player p : winner.getMembers()) {
            RewardsDAO.addRewardAsync(p.getUniqueId().toString(), rewardWin.getName(), rewardWin.getServer(), rewardWin.getCommand());
            p.sendMessage("§aVous avez reçu une récompense pour votre victoire ! (/recompenses)");
        }

        for(Player p : looser.getMembers()) {
            RewardsDAO.addRewardAsync(p.getUniqueId().toString(), rewardParticip.getName(), rewardParticip.getServer(), rewardParticip.getCommand());
            p.sendMessage("§aVous avez reçu une récompense pour votre victoire ! (/recompenses)");
        }

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
