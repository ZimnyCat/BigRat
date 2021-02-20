package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class MessageTime extends Module {

    public MessageTime() {
        super("MessageTime", KEY_UNBOUND, Category.CHAT, "Time in chat messages",
                new SettingToggle("Seconds", true));
    }

    @Subscribe
    public void chat(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameMessageS2CPacket) || ModuleManager.getModule(NameHide.class).isToggled()) return;
        BleachLogger.noPrefixMessage(((GameMessageS2CPacket) e.getPacket()).getMessage().getString());
        e.setCancelled(true);
    }
}
