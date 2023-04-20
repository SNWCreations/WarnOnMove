package snw.warnonmove;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        ItemStack s = event.getPlayer().getInventory().getHelmet();
        if (s != null) {
            //System.out.println("Detected helmet");
            if (s.getType() == Material.STONE_BUTTON) {
                //System.out.println("Playing sound!");
                event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, SoundCategory.AMBIENT, 1, 1);
                //event.getPlayer().getServer().dispatchCommand(event.getPlayer(), "playsound minecraft:entity.villager.ambient ambient @a ~ ~ ~");
                //event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "minecraft:entity.whale.ambient", SoundCategory.MASTER, 1, 0); // pitch should NOT be 0
            } else {
                //System.out.println("NOT stone button, detected " + s);
            }
        }
    }
}
