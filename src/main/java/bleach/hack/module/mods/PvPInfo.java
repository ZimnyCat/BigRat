package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class PvPInfo extends Module {

    int textHeight = 0;

    public PvPInfo() {
        super("PvPInfo", KEY_UNBOUND, Category.COMBAT, "Shows pvp information about players in range",
                new SettingSlider("Range", 1, 100, 50, 1),
                new SettingSlider("Height", 1, 500, 260, 1),
                new SettingSlider("Width", 1, 1000, 2, 1));
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        List<AbstractClientPlayerEntity> players = new ArrayList<>();
        for (Entity p : mc.world.getEntities()) {
            if (p == mc.player || !(p instanceof PlayerEntity) || mc.player.distanceTo(p) > getSetting(0).asSlider().getValue()) continue;
            players.add((AbstractClientPlayerEntity) p);
        }
        if (players.isEmpty()) return;
        for (AbstractClientPlayerEntity p : players) {
            String[] info = {"\u00a73" + p.getDisplayName().asString(), "\u00a7fHP [\u00a73" + Math.round(p.getHealth()) + "\u00a7f]",
                    "\u00a7fPing [\u00a73" + mc.player.networkHandler.getPlayerListEntry(p.getUuid()).getLatency() + "\u00a7f]", ""};
            for (String s : info) {
                mc.textRenderer.drawWithShadow(e.matrix, s, (float) getSetting(2).asSlider().getValue(),
                        (float) getSetting(1).asSlider().getValue() + textHeight, 0xa0a0a0);
                textHeight += 10;
            }
        }
        textHeight = 0;
    }
}
