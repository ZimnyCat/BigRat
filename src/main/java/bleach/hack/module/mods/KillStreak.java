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
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class KillStreak extends Module {

    int kills = 0;
    long killTime = 0;
    public String[] killWords = {"by", "slain", "fucked", "killed", "убит", "separated", "punched", "shoved", "crystal", "nuked"};

    public KillStreak() {
        super("KillStreak", KEY_UNBOUND, Category.COMBAT, "Kill streak",
                new SettingSlider("Height", 1, 500, 2, 1),
                new SettingSlider("Width", 1, 1000, 70, 1),
                new SettingToggle("ChatNotifications", true),
                new SettingToggle("Clear", false));
    }

    @Subscribe
    public void onTick(EventTick eventTick) {
        if (killTime != 0 && (System.currentTimeMillis() - killTime) > 200 && !mc.player.isDead()) {
            kills++;
            if (getSetting(2).asToggle().state) BleachLogger.infoMessage("Kill streak [\u00a73" + kills + "\u00a7f]");
            killTime = 0;
        }
        if (mc.player.isDead()) {
            kills = 0;
            killTime = 0;
        }
        if (getSetting(3).asToggle().state) {
            kills = 0;
            killTime = 0;
            getSetting(3).asToggle().toggle();
        }
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        mc.textRenderer.drawWithShadow(e.matrix, mc.options.debugEnabled ? "" : "\u00a7fKill streak [\u00a73" + kills + "\u00a7f]",
                (float) getSetting(1).asSlider().getValue(), (float) getSetting(0).asSlider().getValue(), 0xffffff);
    }

    @Subscribe
    public void onKill(EventReadPacket event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket)) return;
        String message = ((GameMessageS2CPacket) event.getPacket()).getMessage().getString().toLowerCase();
        for (String s : killWords) {
            if (message.contains(s) && message.contains(mc.player.getName().asString().toLowerCase())
                    && ((GameMessageS2CPacket) event.getPacket()).getSenderUuid().toString().contains("000000000")) {
                killTime = System.currentTimeMillis();
                break;
            }
        }
    }

    @Subscribe
    public void gameJoin(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameJoinS2CPacket)) return;
        kills = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        kills = 0;
        killTime = 0;
    }
}
