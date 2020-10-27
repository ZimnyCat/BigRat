package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;

public class OffhandApple extends Module {

    boolean switched;
    boolean enableAutoTotem;
    Item apple;

    public OffhandApple() {
        super("OffhandApple", KEY_UNBOUND, Category.COMBAT, "apple to offhand free minecraft hack 100% cheat",
                new SettingToggle("SwordOnly", true),
                new SettingMode("AppleType", "God", "Gapple"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (getSetting(1).asMode().mode == 0) {
            apple = Items.ENCHANTED_GOLDEN_APPLE;
        } else {
            apple = Items.GOLDEN_APPLE;
        }
        if (mc.options.keyUse.isPressed() && mc.player != null) {
            //i hate myself
            if (!ModuleManager.getModule(AutoTotem.class).isToggled()
                    && (mc.player.inventory.getMainHandStack().getItem() instanceof SwordItem
                    || !getSetting(0).asToggle().state)) {
                if (!switched) {
                   switched = true;
                   Integer gapSlot = null;
                   for (int slot = 0; slot < 36; slot++) {
                       ItemStack stack = mc.player.inventory.getStack(slot);
                       if (stack.isEmpty() || stack.getItem() != apple)
                           continue;
                       else {
                           gapSlot = slot;
                           break;
                        }
                    }
                    if (gapSlot == null) {
                       return;
                    }
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, gapSlot < 9 ? (gapSlot + 36) : (gapSlot), 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                }
            } else {
                if (mc.player.inventory.getMainHandStack().getItem() instanceof SwordItem
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
            for (slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.inventory.getStack(slot);
                if (stack.isEmpty() || stack.getItem() != Items.TOTEM_OF_UNDYING) {
                    continue;
                } else {
                    noTotems = false;
                    break;
                }
            }
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
