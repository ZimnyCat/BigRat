package bleach.hack.module.mods;


import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.bleacheventbus.BleachSubscribe;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", KEY_UNBOUND, Category.MOVEMENT, "Automatically walks/flies forward");
    }

    public void onDisable() {
        mc.options.keyForward.setPressed(false);
        super.onDisable();
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        mc.options.keyForward.setPressed(true);
    }
}

