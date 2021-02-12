package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTime extends Module {

    public MessageTime() { super("MessageTime", KEY_UNBOUND, Category.CHAT, "Time in chat messages"); }

    @Subscribe
    public void chat(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        BleachLogger.noPrefixMessage("\u00a73[\u00a7f" + time + "\u00a73] \u00a7f" + ((GameMessageS2CPacket) e.getPacket()).getMessage().getString());
        e.setCancelled(true);
    }
}
