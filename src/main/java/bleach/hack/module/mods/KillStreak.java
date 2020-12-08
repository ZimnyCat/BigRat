package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class KillStreak extends Module {

    int kills = 0;

    public KillStreak() {
        super("KillStreak", KEY_UNBOUND, Category.COMBAT, "Kill streak",
                new SettingSlider("Height", 1, 500, 250, 1),
                new SettingToggle("ChatNotification", true));
    }

    @Subscribe
    public void onTick(EventTick eventTick) {
        if (mc.player.isDead() && kills != 0) kills = 0;
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        mc.textRenderer.drawWithShadow(e.matrix, mc.options.debugEnabled ? "" : "Kill streak: " + kills, 2, (float) getSetting(0).asSlider().getValue(), 0xff007c);
    }

    @Subscribe
    public void onKill(EventReadPacket event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket)) return;
        String message = ((GameMessageS2CPacket) event.getPacket()).getMessage().getString().toLowerCase();
        String[] killMsgs = {"by", "slain", "fucked", "killed", "убит", "separated", "punched", "shoved", "crystal"};
        for (String s : killMsgs) {
            if (message.contains(s) && message.contains(mc.player.getName().asString().toLowerCase()) && !mc.player.isDead()
                    && ((GameMessageS2CPacket) event.getPacket()).getSenderUuid() == null) {
                kills++;
                if (getSetting(1).asToggle().state) BleachLogger.infoMessage("Kill streak: \u00a7c" + kills);
                break;
            }
        }
    }
}
