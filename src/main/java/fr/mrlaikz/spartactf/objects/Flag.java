package fr.mrlaikz.spartactf.objects;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.Color;
import fr.mrlaikz.spartactf.enums.Status;
import org.bukkit.Location;
import org.bukkit.Material;

public class Flag {

    private Team team;
    private Location loc;
    private Status status;
    private Material mat;

    public Flag(Team team, Map map, Color c) {
        this.team = team;
        this.loc = map.getFlagLocation(c);
        this.status = Status.FREE;
        this.mat = team.getColor().getMaterial();
    }

    //GETTERS
    public Location getLocation() {
        return loc;
    }

    public Status getStatus() {
        return status;
    }

    public Material getMaterial() {
        return mat;
    }

    //SETTERS
    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

}
