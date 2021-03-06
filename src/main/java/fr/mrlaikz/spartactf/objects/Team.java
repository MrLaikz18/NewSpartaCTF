package fr.mrlaikz.spartactf.objects;

import fr.mrlaikz.spartactf.enums.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private Color color;
    private List<Player> members;
    private Flag flag;

    public Team(Color color, Map map) {
        this.color = color;
        this.members = new ArrayList<>();
        this.flag = new Flag(this, map, color);
    }

    //GETTERS
    public Color getColor() {
        return color;
    }

    public List<Player> getMembers() {
        return members;
    }

    public Flag getFlag() {
        return flag;
    }

    //SETTERS
    public void addPlayer(Player p) {
        members.add(p);
    }

    public void remove(Player p) { members.remove(p); }


}
