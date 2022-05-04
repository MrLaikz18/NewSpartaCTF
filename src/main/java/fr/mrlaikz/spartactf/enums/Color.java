package fr.mrlaikz.spartactf.enums;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.objects.Map;
import org.bukkit.Location;
import org.bukkit.Material;

public enum Color {

    RED("§cRouge", Material.RED_BANNER),
    BLUE("§9Bleue", Material.BLUE_BANNER);

    private String name;
    private Material mat;

    Color(String name, Material mat) {
        this.name = name;
        this.mat = mat;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return mat;
    }

}
