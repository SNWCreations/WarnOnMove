package snw.warnonmove;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EventListener implements Listener {
    private final Plugin p;
    public EventListener(Plugin p) {
        this.p = p;
    }

    private final Set<UUID> inactive = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        ItemStack s = event.getPlayer().getInventory().getHelmet();
        if (s != null) {
            //System.out.println("Detected helmet");
            if (s.getType() == Material.STONE_BUTTON) {

                if (locEqualsXYZ(event.getFrom(), event.getTo())) {
                    return;
                }
                final UUID id = event.getPlayer().getUniqueId();
                if (inactive.contains(id)) {
                    return; // should NOT play at this time
                }
                //System.out.println("Playing sound!");
                event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "entity.whale.ambient", SoundCategory.AMBIENT, 1, 1);
                inactive.add(id);
                Bukkit.getScheduler().runTaskLater(p, () -> inactive.remove(id), 20L); // sound length: 20 tick
                //event.getPlayer().getServer().dispatchCommand(event.getPlayer(), "playsound minecraft:entity.villager.ambient ambient @a ~ ~ ~");
                //event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "minecraft:entity.whale.ambient", SoundCategory.MASTER, 1, 0); // pitch should NOT be 0
            } else {
                //System.out.println("NOT stone button, detected " + s);
            }
        }
    }

    private boolean locEqualsXYZ(Location o, final Location other) {
        if (o == null || other == null) {
            return false;
        }
        if (Double.doubleToLongBits(o.getX()) != Double.doubleToLongBits(other.getX())) {
            return false;
        }
        if (Double.doubleToLongBits(o.getY()) != Double.doubleToLongBits(other.getY())) {
            return false;
        }
        if (Double.doubleToLongBits(o.getZ()) != Double.doubleToLongBits(other.getZ())) {
            return false;
        }
        return true;
    }
}
