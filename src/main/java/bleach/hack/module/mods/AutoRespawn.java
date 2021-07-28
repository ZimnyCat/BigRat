package bleach.hack.module.mods;

import bleach.hack.event.events.EventOpenScreen;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachQueue;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", KEY_UNBOUND, Category.PLAYER, "Automatically respawn when you die",
                new SettingToggle("Delay", false),
                new SettingSlider("Delay", 1, 15, 5, 0));
    }

    @BleachSubscribe
    public void onOpenScreen(EventOpenScreen event) {
        if (event.getScreen() instanceof DeathScreen) {
            if (getSetting(0).asToggle().state) {
                for (int i = 0; i <= (int) getSetting(1).asSlider().getValue(); i++)
                    BleachQueue.add("autorespawn", () -> {
                    });
                BleachQueue.add("autorespawn", () -> mc.player.requestRespawn());
            } else {
                mc.player.requestRespawn();
            }
        }
    }
}
