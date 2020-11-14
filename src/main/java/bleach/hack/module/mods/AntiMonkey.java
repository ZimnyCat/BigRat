package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AntiMonkey extends Module {

    //TODO: add more monkey sounds
    String[] monkeySounds = {"ez", "nn"};

    public AntiMonkey() {
        super("AntiMonkey", KEY_UNBOUND, Category.CHAT, "Mutes pvp monkeys");
    }

    @Subscribe
    public void chatMessage(EventReadPacket event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket)) return;
        String message = ((GameMessageS2CPacket) event.getPacket()).getMessage().asString().toLowerCase();
        for (String s : monkeySounds) {
            boolean cancel;
            cancel = message.contains(s) ? true : false;
            event.setCancelled(cancel);
        }
    }
}
