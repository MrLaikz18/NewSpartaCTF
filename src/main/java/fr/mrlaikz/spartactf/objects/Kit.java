package fr.mrlaikz.spartactf.objects;

import fr.mrlaikz.spartactf.SpartaCTF;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {

    private String name;
    private String displayName;
    private ItemStack[] armor = new ItemStack[3];
    private ItemStack[] content = new ItemStack[9];
    private Material mat;

    public Kit(String name) {
        this.name = name;
        this.displayName = SpartaCTF.strConfig("kits." + name + ".name");
        this.mat = Material.matchMaterial(SpartaCTF.getInstance().getConfig().getString("kits." + name + ".material"));

        int i = 0;
        for(String it : SpartaCTF.getInstance().getConfig().getConfigurationSection("kits." + name + ".armor").getKeys(false)) {
            ItemStack its = SpartaCTF.getInstance().getConfig().getItemStack("kits." + name + ".armor." + i);
            armor[i] = its;
            i++;
        }

        i = 0;
        for(String it : SpartaCTF.getInstance().getConfig().getConfigurationSection("kits." + name + ".content").getKeys(false)) {
            ItemStack its = SpartaCTF.getInstance().getConfig().getItemStack("kits." + name + ".content." + i);
            content[i] = its;
            i++;
        }

    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return mat;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public void give(Player p) {
        p.getInventory().setArmorContents(content);
        for(ItemStack it : content) {
            p.getInventory().addItem(it);
        }
    }

}
