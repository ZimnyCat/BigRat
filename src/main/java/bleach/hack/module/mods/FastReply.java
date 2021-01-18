package bleach.hack.module.mods;

import bleach.hack.command.Command;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventSendPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class FastReply extends Module {

    String name;

    public FastReply() { super("FastReply", KEY_UNBOUND, Category.CHAT, "Easy and fast reply to direct messages"); }

    @Subscribe
    public void message(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;
        String msg = ((GameMessageS2CPacket) e.getPacket()).getMessage().getString();
        for (PlayerListEntry p : mc.player.networkHandler.getPlayerList()) {
            if (p.getProfile() == mc.player.getGameProfile()) continue;
            if (msg.startsWith(p.getProfile().getName() + " whispers")) name = p.getProfile().getName();
        }
    }

    @Subscribe
    public void send(EventSendPacket e) {
        if (!(e.getPacket() instanceof ChatMessageC2SPacket)) return;
        System.out.println("test");
        String msg = ((ChatMessageC2SPacket) e.getPacket()).getChatMessage();
        if (msg.startsWith("/") || msg.startsWith(Command.PREFIX) || name == null) return;
        mc.player.sendChatMessage("/msg " + name + " " + msg);
        name = null;
        e.setCancelled(true);
    }
}
