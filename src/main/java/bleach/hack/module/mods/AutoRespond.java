package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.List;
import java.util.Random;

public class AutoRespond extends Module {
    public AutoRespond() {
        super("AutoRespond", KEY_UNBOUND, Category.CHAT, "Automatically responds to chat messages from selected player(s)");
    }

    public void init() {
        String[] arFiles = {"players", "messages", "offlineFormat"};
        for (String s : arFiles) BleachFileMang.createFile("ar_" + s + ".txt");
        if (BleachFileMang.readFileLines("ar_offlineFormat.txt").isEmpty())
            BleachFileMang.appendFile("<%name%>", "ar_offlineFormat.txt");
    }
    @Subscribe
    public void onChatMessage(EventReadPacket e) {
        MobOwner mo = new MobOwner();
        Random r = new Random();
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;
        List<String> gamers = BleachFileMang.readFileLines("ar_players.txt");
        List<String> msgs = BleachFileMang.readFileLines("ar_messages.txt");
        String msg = ((GameMessageS2CPacket) e.getPacket()).getMessage().getString().toLowerCase();
        if (gamers.isEmpty()) disable("ar_players.txt");
        if (msgs.isEmpty()) disable("ar_messages.txt");
        if (gamers.contains(mo.getNameFromUUID(((GameMessageS2CPacket) e.getPacket()).getSenderUuid().toString()))) {
            mc.player.sendChatMessage(msgs.get(r.nextInt(msgs.size())));
            return;
        }
        for (String s : gamers) {
            String formatted = BleachFileMang.readFileLines("ar_offlineFormat.txt").toString().toLowerCase().replace("%name%", s.toLowerCase());
            if (msg.startsWith(formatted.toLowerCase().replace("[", "").replace("]", "")))
                mc.player.sendChatMessage(msgs.get(r.nextInt(msgs.size())));
        }
    }
    private void disable(String fileName) {
        BleachLogger.errorMessage("./BigRat/" + fileName +" is empty! Disabling AutoRespond...");
        BleachLogger.infoMessage("Check BigRat in your Minecraft folder!");
        setToggled(false);
    }
}
