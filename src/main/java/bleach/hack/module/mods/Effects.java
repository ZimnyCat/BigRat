package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachJsonHelper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffectInstance;

public class Effects extends Module {

    int textHeight = 0;

    public Effects() {
        super("Effects", KEY_UNBOUND, Category.PLAYER, "Does exactly what you think it does",
                new SettingSlider("Height", 1, 500, 2, 1),
                new SettingSlider("Width", 1, 1000, 220, 1),
                new SettingToggle("Notify", false).withDesc("Notifies when effect is over"));
    }

    @Subscribe
    public void onDraw(EventDrawOverlay event) {
        for (StatusEffectInstance se : mc.player.getStatusEffects()) {
            // it spams but ok
            // (bigrat is the worst client ever)
            if (se.getDuration() == 0 && getSetting(2).asToggle().state)
                BleachLogger.infoMessage("\u00a73" + se.getEffectType().getName().getString() + " is over");
            mc.textRenderer.drawWithShadow(event.matrix,
                    "\u00a7f" + se.getEffectType().getName().getString() + " \u00a73" + (se.getAmplifier() + 1) +
                            " \u00a7f[\u00a73" + Math.round((float) se.getDuration() / 20) + "\u00a7f]",
                    (float) getSetting(1).asSlider().getValue(), (float) getSetting(0).asSlider().getValue() + textHeight, 0xa0a0a0);
            textHeight += 10;
        }
        textHeight = 0;
    }
}
