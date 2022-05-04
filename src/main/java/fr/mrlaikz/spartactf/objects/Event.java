package fr.mrlaikz.spartactf.objects;

import fr.iban.bukkitcore.rewards.Reward;
import fr.iban.bukkitcore.rewards.RewardsDAO;
import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.Color;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.enums.Status;
import fr.mrlaikz.spartactf.interfaces.ListenerInterface;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
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
        teams.add(new Team(Color.BLUE, map));
        teams.add(new Team(Color.RED, map));
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
        if(getEnemyTeam(team).getMembers().contains(p)) {
            getEnemyTeam(team).remove(p);
        }
        team.addPlayer(p);
    }

    public void leavePlayer(Player p) {
        Team red = getTeam(Color.RED);
        Team blue = getTeam(Color.BLUE);
        if(red.getMembers().contains(p)) {
            red.remove(p);
        }
        if(blue.getMembers().contains(p)) {
            blue.remove(p);
        }
        p.getInventory().clear();
    }

    public void joinRandomPlayer(Player p) {
        Team red = getTeam(Color.RED);
        Team blue = getTeam(Color.BLUE);
            if (red.getMembers().size() <= blue.getMembers().size()) {
                joinPlayer(p, red);
                p.sendMessage("§cTu as rejoint l'équipe §c§lROUGE");
            } else {
                joinPlayer(p, blue);
                p.sendMessage("§cTu as rejoint l'équipe §9§lBLEUE");
            }
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
        capturer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
        Bukkit.broadcastMessage("§aLe drapeau " + captured.getColor().getName() + " §aa été capturé !");
    }

    public void fallFlag(Player capturer, Team captured, Location loc) {
        capturer.getLocation().getBlock().setType(capturer.getInventory().getHelmet().getType());
        capturer.getInventory().setHelmet(new ItemStack(Material.AIR));
        captured.getFlag().setStatus(Status.FREE);
        captured.getFlag().setLocation(loc);
        capturer.removePotionEffect(PotionEffectType.GLOWING);
        Bukkit.broadcastMessage("§aLe drapeau " + captured.getColor().getName() + " §aest maintenant libre !");
    }

    public void resetFlag(Team team) {
        team.getFlag().getLocation().getBlock().setType(Material.AIR);
        map.getFlagLocation(team.getColor()).getBlock().setType(team.getFlag().getMaterial());
        team.getFlag().setStatus(Status.FREE);
        team.getFlag().setLocation(map.getFlagLocation(team.getColor()));
        Bukkit.broadcastMessage("§aLe drapeau " + team.getColor().getName() + " §aa été reset");
    }

    ///LISTENERS MANAGER
    @Override
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        if(state.equals(EventState.PLAYING)) {
            Team playerTeam = getTeamFromPlayer(p);
            Team enemyTeam = getEnemyTeam(playerTeam);

            if(playerTeam != null && enemyTeam != null) {
                if (matchLocations(to, enemyTeam.getFlag().getLocation()) && enemyTeam.getFlag().getStatus().equals(Status.FREE)) {
                    captureFlag(p, enemyTeam);
                }

                if (p.getInventory().getHelmet() != null) {
                    if (matchLocations(to, map.getFlagLocation(playerTeam.getColor())) && p.getInventory().getHelmet().getType().equals(enemyTeam.getFlag().getMaterial())) {
                        stop(playerTeam, enemyTeam);
                    }
                }

                if (playerTeam.getFlag().getStatus().equals(Status.FREE) && matchLocations(to, playerTeam.getFlag().getLocation()) && !matchLocations(playerTeam.getFlag().getLocation(), map.getFlagLocation(playerTeam.getColor()))) {
                    resetFlag(playerTeam);
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Team playerTeam = getTeamFromPlayer(p);
        Team enemyTeam = getEnemyTeam(playerTeam);

        if(playerTeam != null && enemyTeam != null) {
            if (p.getInventory().getHelmet() != null) {
                if (p.getInventory().getHelmet().getType().equals(enemyTeam.getFlag().getMaterial()) && enemyTeam.getFlag().getStatus().equals(Status.TAKEN)) {
                    fallFlag(p, enemyTeam, p.getLocation());
                    p.setBedSpawnLocation(map.getFlagLocation(playerTeam.getColor()));
                }
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().hasItemMeta()) {
            Team red = getTeam(Color.RED);
            Team blue = getTeam(Color.BLUE);
            if (p.getInventory().getItemInMainHand().getType().equals(Material.RED_WOOL)) {
                if (red.getMembers().size() <= blue.getMembers().size()) {
                    joinPlayer(p, red);
                    p.sendMessage("§cTu as rejoint l'équipe §c§lROUGE");
                } else {
                    p.sendMessage("§4Impossible de rejoindre l'équipe !");
                }

            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.BLUE_WOOL)) {
                if (red.getMembers().size() >= blue.getMembers().size()) {
                    joinPlayer(p, blue);
                    p.sendMessage("§9Tu as rejoint l'équipe §9§lBLEUE");
                } else {
                    p.sendMessage("§4Impossible de rejoindre l'équipe !");
                }
            }
        }
        e.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();

        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta redM = red.getItemMeta();
        redM.setDisplayName("§cRejoindre l'équipe §c§lRouge");
        red.setItemMeta(redM);

        ItemStack blue = new ItemStack(Material.BLUE_WOOL);
        ItemMeta blueM = blue.getItemMeta();
        blueM.setDisplayName("§9Rejoindre l'équipe §9§lBleue");
        blue.setItemMeta(blueM);

        p.getInventory().addItem(red);
        p.getInventory().addItem(blue);

    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.teleport(map.getFlagLocation(getTeamFromPlayer(p).getColor()));
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent e) {
        Player p = (Player) e.getDamager();
        Player c = (Player) e.getEntity();
        if(getState().equals(EventState.PLAYING)) {
            if(p != null && c != null) {
                Team pTeam = getTeamFromPlayer(p);
                Team cTeam = getTeamFromPlayer(c);
                if(pTeam != null && cTeam != null) {
                    if (pTeam == cTeam) {
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(false);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerLeave(PlayerQuitEvent e) {
        leavePlayer(e.getPlayer());
    }

    ///EVENT MANAGEMENT
    public void start() {
        spawnFlags();
        state = EventState.PLAYING;

        for(Player pl : Bukkit.getOnlinePlayers()) {
            if(pl.getGameMode().equals(GameMode.ADVENTURE)) {
                if(!getTeam(Color.RED).getMembers().contains(pl) && !getTeam(Color.BLUE).getMembers().contains(pl)) {
                    joinRandomPlayer(pl);
                }
            }
        }

        for(Team t : teams) {
            for(Player p : t.getMembers()) {
                p.teleport(map.getFlagLocation(t.getColor()));
                p.getInventory().clear();
                giveStuff(t, p);
            }
        }



    }

    public void stop(Team winner, Team looser) {
        this.state = null;
        SpartaCTF.getInstance().getEventManager().stopEvent(this);

        for(Player p : winner.getMembers()) {
            RewardsDAO.addRewardAsync(p.getUniqueId().toString(), rewardWin.getName(), rewardWin.getServer(), rewardWin.getCommand());
            p.sendMessage("§aVous avez reçu une récompense pour votre victoire ! (/recompenses)");
            p.teleport(map.getSpawnLocation());
            p.getInventory().clear();
            if(p.hasPotionEffect(PotionEffectType.GLOWING)) {
                p.removePotionEffect(PotionEffectType.GLOWING);
            }
        }

        for(Player p : looser.getMembers()) {
            RewardsDAO.addRewardAsync(p.getUniqueId().toString(), rewardParticip.getName(), rewardParticip.getServer(), rewardParticip.getCommand());
            p.sendMessage("§aVous avez reçu une récompense pour votre victoire ! (/recompenses)");
            p.teleport(map.getSpawnLocation());
            p.getInventory().clear();
        }

        Bukkit.broadcastMessage("§aL'équipe " + winner.getColor().getName() + " §agagne l'event !");


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

    public void giveStuff(Team t, Player p) {

        if(t.getColor().equals(Color.RED)) {

            ItemStack chestplate = SpartaCTF.getInstance().getConfig().getItemStack("stuff.red.armor.chestplate");
            ItemStack leggings = SpartaCTF.getInstance().getConfig().getItemStack("stuff.red.armor.leggings");
            ItemStack boots = SpartaCTF.getInstance().getConfig().getItemStack("stuff.red.armor.boots");

            p.getInventory().setChestplate(chestplate);
            p.getInventory().setLeggings(leggings);
            p.getInventory().setBoots(boots);

            for(String s : SpartaCTF.getInstance().getConfig().getConfigurationSection("stuff.red.stuff").getKeys(false)) {
                ItemStack it = SpartaCTF.getInstance().getConfig().getItemStack("stuff.red.stuff." + s);
                p.getInventory().addItem(it);
            }

        } else if(t.getColor().equals(Color.BLUE)) {

            ItemStack chestplate = SpartaCTF.getInstance().getConfig().getItemStack("stuff.blue.armor.chestplate");
            ItemStack leggings = SpartaCTF.getInstance().getConfig().getItemStack("stuff.blue.armor.leggings");
            ItemStack boots = SpartaCTF.getInstance().getConfig().getItemStack("stuff.blue.armor.boots");

            p.getInventory().setChestplate(chestplate);
            p.getInventory().setLeggings(leggings);
            p.getInventory().setBoots(boots);

            for(String s : SpartaCTF.getInstance().getConfig().getConfigurationSection("stuff.blue.stuff").getKeys(false)) {
                ItemStack it = SpartaCTF.getInstance().getConfig().getItemStack("stuff.blue.stuff." + s);
                p.getInventory().addItem(it);
            }

        }


    }



}
