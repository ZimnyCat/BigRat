package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoNametag extends Module {
    public AutoNametag() {
        super("AutoNametag", KEY_UNBOUND, Category.MISC, "Nametags entities",
                new SettingSlider("Range", 1, 5, 3, 0));
    }

    @Subscribe
    public void onTick(EventTick eventTick) {
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof MobEntity)
                    || mc.player.distanceTo(entity) > getSetting(0).asSlider().getValue()
                    || entity.hasCustomName()) continue;
            if (mc.player.getMainHandStack().getItem() != Items.NAME_TAG) {
                Integer nameTagSlot = Finder.find(Items.NAME_TAG, true);
                if (nameTagSlot == null) {
                    BleachLogger.infoMessage("No nametags found in hotbar! Disabling AutoNametag...");
                    setToggled(false);
                } else mc.player.inventory.selectedSlot = nameTagSlot;
            }
            mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND);
        }
    }
}
