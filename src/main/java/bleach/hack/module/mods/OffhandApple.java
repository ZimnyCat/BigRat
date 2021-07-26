package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;

public class OffhandApple extends Module {

    boolean switched;
    boolean enableAutoTotem;
    Item apple;

    public OffhandApple() {
        super("OffhandApple", KEY_UNBOUND, Category.COMBAT, "apple to offhand free minecraft hack 100% cheat",
                new SettingToggle("SwordOnly", false),
                new SettingMode("AppleType", "God", "Golden"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        apple = getSetting(1).asMode().mode == 0 ? Items.ENCHANTED_GOLDEN_APPLE : Items.GOLDEN_APPLE;
        if (mc.options.keyUse.isPressed() && mc.player != null) {
            Integer gapSlot = Finder.find(apple, false);
            if (gapSlot == null) return;
            //i hate myself
            if (!ModuleManager.getModule(AutoTotem.class).isToggled()
                    && (mc.player.getInventory().getMainHandStack().getItem() instanceof SwordItem
                    || !getSetting(0).asToggle().state)) {
                if (!switched) {
                   switched = true;
                   if (gapSlot == null) return;
                   mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                   mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, gapSlot < 9 ? (gapSlot + 36) : (gapSlot), 0, SlotActionType.PICKUP, mc.player);
                   mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                }
            } else {
                if (mc.player.getInventory().getMainHandStack().getItem() instanceof SwordItem
                        || !getSetting(0).asToggle().state) {
                    if (ModuleManager.getModule(AutoTotem.class).isToggled()) {
                        enableAutoTotem = true;
                    }
                    ModuleManager.getModule(AutoTotem.class).setToggled(false);
                }
            }
        }
        else if (enableAutoTotem){
            enableAutoTotem = false;
            Integer slot = null;
            boolean noTotems = true;
            if (Finder.find(Items.TOTEM_OF_UNDYING, false) != null) noTotems = false;
            if (!noTotems) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
            }
            ModuleManager.getModule(AutoTotem.class).setToggled(true);
            switched = false;
        } else {
            switched = false;
        }
    }
}
