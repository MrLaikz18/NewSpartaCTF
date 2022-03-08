package fr.mrlaikz.spartactf.menus;

import fr.iban.bukkitcore.menu.Menu;
import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.objects.Kit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitMenu extends Menu {

    private SpartaCTF plugin;
    private FileConfiguration config;

    public KitMenu(Player player, SpartaCTF plugin) {
        super(player);
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public String getMenuName() {
        return "§6Kits";
    }

    @Override
    public int getRows() {
        return 1;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        try {
            Kit k = getByDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
            k.give(p);
        } catch(NullPointerException ex) {
            ex.printStackTrace();
            p.sendMessage("§4Impossible de trouver ce kit !");
        }

    }

    @Override
    public void setMenuItems() {

        for(String s : config.getConfigurationSection("kits").getKeys(false)) {
            Kit k = new Kit(s);
            ItemStack it = new ItemStack(k.getMaterial());
            ItemMeta itM = it.getItemMeta();
            itM.setDisplayName(k.getDisplayName());
            it.setItemMeta(itM);
            inventory.addItem(it);
        }

    }

    public Kit getByDisplayName(String name) throws NullPointerException {
        for(String s : config.getConfigurationSection("kits").getKeys(false)) {
            Kit k = new Kit(s);
            if(k.getDisplayName().equals(s)) {
                return k;
            }
        }
        return null;
    }
}
