package snw.warnonmove;

import org.bukkit.Material;
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
            if (s.getType() == Material.STONE_BUTTON) {
                event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "minecraft:entity.whale.ambient", SoundCategory.MASTER, 1, 0);
            }
        }
    }
}
