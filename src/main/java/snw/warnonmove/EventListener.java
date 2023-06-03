package snw.warnonmove;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventListener implements Listener {
    private static final String NOTICE_TARGET = "Murasame_mao";
    private final Plugin p;

    public EventListener(Plugin p) {
        this.p = p;
    }

    private final Set<UUID> inactive = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        //System.out.println("Detected helmet");
        if (shouldWarn(event.getPlayer())) {

            if (locEqualsXYZ(event.getFrom(), event.getTo())) {
                return;
            }
            final UUID id = event.getPlayer().getUniqueId();
            if (inactive.contains(id)) {
                return; // should NOT play at this time
            }
            //System.out.println("Playing sound!");
            event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "entity.whale.ambient", SoundCategory.MASTER, 1, 1);
            inactive.add(id);
            Bukkit.getScheduler().runTaskLater(p, () -> inactive.remove(id), 20L); // sound length: 20 tick
            //event.getPlayer().getServer().dispatchCommand(event.getPlayer(), "playsound minecraft:entity.villager.ambient ambient @a ~ ~ ~");
            //event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), "minecraft:entity.whale.ambient", SoundCategory.MASTER, 1, 0); // pitch should NOT be 0
        } else {
            //System.out.println("NOT stone button, detected " + s);
        }

    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (shouldWarn(player)) {
                final ItemStack stack = event.getItem().getItemStack();
                final Material material = stack.getType();
                if (Objects.equals(material.getKey().toString(), "tzz:jiechuka")) {
                    Player giver = Bukkit.getPlayer(event.getItem().getThrower());
                    System.out.println(event.getItem().hashCode());
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "你的警报已被解除，并成功与给予你解除卡的逃走者组队成功。");
                    Player notice = Bukkit.getPlayer(NOTICE_TARGET);
                    if (notice != null) { // online
                        notice.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + player.getName() + " 的警报已解除。给予 TA 解除卡的人是: " + giver.getName());
                    } // or not online lol
                    player.getInventory().setHelmet(null); // let the warning does not work!
                    event.setCancelled(true);
                    event.getItem().remove();
                }
            }
        }
    }

    private boolean shouldWarn(Player player) {
        return Optional.ofNullable(player.getInventory().getHelmet())
                .map(i -> i.getType() == Material.STONE_BUTTON)
                .orElse(false);
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String JUST_MY_AD___PLEASE_RESPECT_MY_WORK = "unused";
        sendMessages(event.getPlayer(),
                "欢迎！插件正在正常运行！",
                "作者: ZX夏夜之风 (B站: @ZX夏夜之风)",
                "版权所有 (C) 2023 SNWCreations. 保留所有权利。"
        );
    }

    private void sendMessages(Player player, String... msgs) {
        for (String msg : msgs) {
            player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "WarnOnMove" + ChatColor.GRAY + "] " + ChatColor.GREEN + msg);
        }
    }
}
