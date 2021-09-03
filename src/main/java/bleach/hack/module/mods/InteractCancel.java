package bleach.hack.module.mods;

import bleach.hack.event.events.EventSendPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

import java.util.HashMap;
import java.util.Map;

public class InteractCancel extends Module {

    HashMap<Integer, Item> items = new HashMap<>();

    public InteractCancel() {
        super("InteractCancel", KEY_UNBOUND, Category.PLAYER, "Cancels block interactions",
                new SettingToggle("Gapple", true), // 0
                new SettingToggle("Crystal", false), // 1
                new SettingToggle("Sword", true), // 2
                new SettingToggle("All", false)); // 3
    }

    @Subscribe
    public void interact(EventSendPacket e) {
        if (!(e.getPacket() instanceof PlayerInteractBlockC2SPacket)) return;
        if (getSetting(3).asToggle().state) {
            e.setCancelled(true);
            return;
        }

        items.put(0, Items.ENCHANTED_GOLDEN_APPLE);
        items.put(1, Items.END_CRYSTAL);
        items.put(2, Items.DIAMOND_SWORD);

        for (Map.Entry map : items.entrySet()) {
            if (getSetting((Integer) map.getKey()).asToggle().state
                    && (mc.player.getMainHandStack().getItem() == map.getValue() || mc.player.getOffHandStack().getItem() == map.getValue())) {
                e.setCancelled(true);
                return;
            }
        }
        items.clear();
    }

}
