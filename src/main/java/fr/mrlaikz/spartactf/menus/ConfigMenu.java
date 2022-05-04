package fr.mrlaikz.spartactf.menus;

import fr.iban.bukkitcore.CoreBukkitPlugin;
import fr.iban.bukkitcore.menu.Menu;
import fr.iban.bukkitcore.menu.RewardSelectMenu;
import fr.iban.bukkitcore.rewards.RewardsDAO;
import fr.iban.bukkitcore.utils.SLocationUtils;
import fr.iban.common.teleport.EventAnnouce;
import fr.mrlaikz.spartactf.SpartaCTF;
import fr.mrlaikz.spartactf.enums.EventState;
import fr.mrlaikz.spartactf.objects.Event;
import fr.mrlaikz.spartactf.schedules.StartTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu extends Menu {

    private SpartaCTF plugin;
    private Event event;

    public ConfigMenu(Player player, SpartaCTF plugin, Event event) {
        super(player);
        this.plugin = plugin;
        this.event = event;
    }

    @Override
    public String getMenuName() {
        return "§6Menu de Configuration";
    }

    @Override
    public int getRows() {
        return 1;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        CoreBukkitPlugin core = CoreBukkitPlugin.getInstance();

        if (displayNameEquals(item, "§2§lRécompense")) {
            RewardsDAO.getTemplateRewardsAsync().thenAccept(rewards -> {
                Bukkit.getScheduler().runTask(core, () -> new RewardSelectMenu(player, rewards, reward -> {
                    event.setRewardWin(reward);
                    open();
                }).open());
            });
        } else if (displayNameEquals(item, "§2§lRécompense de participation")) {
            RewardsDAO.getTemplateRewardsAsync().thenAccept(rewards -> {
                Bukkit.getScheduler().runTask(core, () -> new RewardSelectMenu(player, rewards, reward -> {
                    event.setRewardParticip(reward);
                    open();
                }).open());
            });
        } else if (displayNameEquals(item, "§6§lAnnoncer")) {
            core.getRedisClient().getTopic("EventAnnounce").publish(new EventAnnouce("Capture The Flag", event.getMap().getName(), "Capturez le drapeau enemi en premier, et ramenez le a votre base !", SLocationUtils.getSLocation(event.getMap().getSpawnLocation()), player.getName()));
        } else if (displayNameEquals(item, "§2§lLancer !")) {
            new StartTask(event).runTaskTimer(core, 0L, 20L);
        }
    }

    @Override
    public void setMenuItems() {

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestM = chest.getItemMeta();
        chestM.setDisplayName("§2§lRécompense");
        if(event.getRewardWin() != null) {
            List<String> lore = new ArrayList<String>();
            lore.add("§7" + event.getRewardWin().getName());
            chestM.setLore(lore);
        }
        chest.setItemMeta(chestM);

        ItemStack chest2 = new ItemStack(Material.CHEST);
        ItemMeta chestM2 = chest.getItemMeta();
        chestM2.setDisplayName("§2§lRécompense de participation");
        if(event.getRewardParticip() != null) {
            List<String> lore = new ArrayList<String>();
            lore.add("§7" + event.getRewardParticip().getName());
            chestM2.setLore(lore);
        }
        chest2.setItemMeta(chestM2);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperM = paper.getItemMeta();
        paperM.setDisplayName("§6§lAnnoncer");
        paper.setItemMeta(paperM);

        ItemStack limedye = new ItemStack(Material.LIME_DYE);
        ItemMeta limedyeM = limedye.getItemMeta();
        limedyeM.setDisplayName("§2§lLancer !");
        limedye.setItemMeta(limedyeM);

        inventory.setItem(1, chest);
        inventory.setItem(2, chest2);
        inventory.setItem(7, paper);
        inventory.setItem(8, limedye);

    }


}
