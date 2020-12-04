package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class KillStreak extends Module {

    int kills = 0;

    public KillStreak() { super("KillStreak", KEY_UNBOUND, Category.COMBAT, "Kill streak"); }

    @Subscribe
    public void onTick(EventTick eventTick) {
        if (mc.player.isDead() && kills != 0) kills = 0;
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        mc.textRenderer.drawWithShadow(e.matrix, "Kills: " + kills, 2, 250, 0xff007c);
    }

    @Subscribe
    public void onKill(EventReadPacket event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket)) return;
        String message = ((GameMessageS2CPacket) event.getPacket()).getMessage().getString();
        String[] killMsgs = {" by ", " slain ", " fucked "};
        for (String s : killMsgs) {
            if (message.contains(s) && message.contains(mc.player.getName().asString()) && !mc.player.isDead()) {
                kills++;
                break;
            }
        }
    }
}
