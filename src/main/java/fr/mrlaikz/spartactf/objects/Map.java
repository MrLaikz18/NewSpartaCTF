package fr.mrlaikz.spartactf.objects;

import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map {

    private String name;
    private Location spawnLocation;
    private List<Location> flagLocations;

    public Map(String name) {
        this.name = name;
        this.spawnLocation = SpartaCTF.getInstance().getConfig().getLocation("maps." + name.toLowerCase(Locale.ROOT) + ".spawn_location");
        this.flagLocations = new ArrayList<>();
        flagLocations.add(SpartaCTF.getInstance().getConfig().getLocation("maps." + name.toLowerCase(Locale.ROOT) + ".red_location"));
        flagLocations.add(SpartaCTF.getInstance().getConfig().getLocation("maps." + name.toLowerCase(Locale.ROOT) + ".blue_location"));
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getFlagLocation(Color c) {
        if(c.equals(Color.RED)) {
            return flagLocations.get(0);
        } else if(c.equals(Color.BLUE)) {
            return flagLocations.get(1);
        }
        return null;
    }

}
