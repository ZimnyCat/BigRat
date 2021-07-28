package bleach.hack.module.mods;

import bleach.hack.event.events.EventEntityRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.entity.mob.MobEntity;

public class VisibleNametags extends Module {
    public VisibleNametags() {
        super("VisibleNametags", KEY_UNBOUND, Category.MISC, "Makes nametags of mobs always visible");
    }
    @BleachSubscribe
    public void onEntity(EventEntityRender event) {
        event.getEntity().setCustomNameVisible(event.getEntity() instanceof MobEntity && event.getEntity().hasCustomName());
    }
}
