package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class MidClickPearl extends Module {
    public MidClickPearl() { super("MidClickPearl", KEY_UNBOUND, Category.MISC, "Throws an ender pearl by pressing the middle mouse button"); }

    @Subscribe
    public void onTick(EventTick e) {
        if (!mc.options.keyPickItem.isPressed()) return;
        Integer pearlSlot = Finder.find(Items.ENDER_PEARL, true);
        if (pearlSlot == null) {
            BleachLogger.infoMessage("No ender pearls found in hotbar!");
            return;
        }
        int slot = mc.player.inventory.selectedSlot;
        mc.player.inventory.selectedSlot = pearlSlot;
        mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
        mc.player.inventory.selectedSlot = slot;
    }
}
