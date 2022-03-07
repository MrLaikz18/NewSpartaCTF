package fr.mrlaikz.spartactf.enums;

import fr.mrlaikz.spartactf.SpartaCTF;
import org.bukkit.Location;
import org.bukkit.Material;

public enum Color {

    RED("§cRouge", SpartaCTF.getInstance().getConfig().getLocation("locations.red"), Material.RED_BANNER),
    BLUE("§9Rouge", SpartaCTF.getInstance().getConfig().getLocation("locations.blue"), Material.BLUE_BANNER);

    private String name;
    private Location loc;
    private Material mat;

    Color(String name, Location loc, Material mat) {
        this.name = name;
        this.loc = loc;
        this.mat = mat;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return loc;
    }

    public Material getMaterial() {
        return mat;
    }

}
