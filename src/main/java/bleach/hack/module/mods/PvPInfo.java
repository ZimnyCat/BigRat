package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.PlayerEntity;

public class PvPInfo extends Module {

    int textHeight = 0;

    public PvPInfo() {
        super("PvPInfo", KEY_UNBOUND, Category.COMBAT, "Shows information about closest player in range",
                new SettingSlider("Range", 1, 8, 5, 1),
                new SettingSlider("Height", 1, 500, 260, 1),
                new SettingSlider("Width", 1, 1000, 2, 1));
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        PlayerEntity player = mc.world.getClosestPlayer(mc.player, getSetting(0).asSlider().getValue());
        if (player == null) return;
        String[] info = {"\u00a73" + player.getDisplayName().asString(), "\u00a7fHP [\u00a73" + Math.round(player.getHealth()) + "\u00a7f]",
                "\u00a7fPing [\u00a73" + mc.player.networkHandler.getPlayerListEntry(player.getUuid()).getLatency() + "\u00a7f]"};
        for (String s : info) {
            mc.textRenderer.drawWithShadow(e.matrix, s, (float) getSetting(2).asSlider().getValue(),
                    (float) getSetting(1).asSlider().getValue() + textHeight, 0xa0a0a0);
            textHeight += 10;
        }
        textHeight = 0;
    }
}
