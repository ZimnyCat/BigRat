package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class NameHide extends Module {

    public NameHide() { super("NameHide", KEY_UNBOUND, Category.CHAT, "Hides your name in chat"); }

    @Subscribe
    public void chat(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;

        String message = ((GameMessageS2CPacket) e.getPacket()).getMessage().getString()
                .replace(mc.player.getDisplayName().getString(), "Me")
                .replace(mc.player.getDisplayName().getString().toLowerCase(), "Me");
        BleachLogger.noPrefixMessage(message);
        e.setCancelled(true);
    }
}
