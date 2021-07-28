package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachFileMang;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoRespond extends Module {

    List<String> gamers = new ArrayList<>();
    List<String> msgs = new ArrayList<>();
    String offlineFormat;

    public AutoRespond() {
        super("AutoRespond", KEY_UNBOUND, Category.CHAT, "Automatically responds to chat messages from selected player(s)",
                new SettingToggle("Reload", false));
    }

    public void init() {
        String[] arFiles = {"players", "messages", "offlineFormat"};
        for (String s : arFiles) BleachFileMang.createFile("ar_" + s + ".txt");
        if (BleachFileMang.readFileLines("ar_offlineFormat.txt").isEmpty())
            BleachFileMang.appendFile("<%name%>", "ar_offlineFormat.txt");
        offlineFormat = BleachFileMang.readFileLines("ar_offlineFormat.txt").toString().toLowerCase();
        offlineFormat = offlineFormat.substring(1, offlineFormat.length() - 1);
        gamers = BleachFileMang.readFileLines("ar_players.txt");
        msgs = BleachFileMang.readFileLines("ar_messages.txt");
    }
    @BleachSubscribe
    public void onChatMessage(EventReadPacket e) {
        MobOwner mo = new MobOwner();
        Random r = new Random();
        if (!(e.getPacket() instanceof GameMessageS2CPacket)) return;
        String msg = ((GameMessageS2CPacket) e.getPacket()).getMessage().getString().toLowerCase();
        if (gamers.isEmpty()) disable("ar_players.txt");
        if (msgs.isEmpty()) disable("ar_messages.txt");
        if (gamers.contains(mo.getNameFromUUID(((GameMessageS2CPacket) e.getPacket()).getSender().toString()))) {
            mc.player.sendChatMessage(msgs.get(r.nextInt(msgs.size())));
            return;
        }
        for (String s : gamers) {
            String formatted = offlineFormat.replace("%name%", s.toLowerCase());
            if (msg.startsWith(formatted)) mc.player.sendChatMessage(msgs.get(r.nextInt(msgs.size())));
        }
    }

    @BleachSubscribe
    public void onTick(EventTick e) {
        if (getSetting(0).asToggle().state) {
            offlineFormat = BleachFileMang.readFileLines("ar_offlineFormat.txt").toString().toLowerCase();
            offlineFormat = offlineFormat.substring(1, offlineFormat.length() - 1);
            gamers = BleachFileMang.readFileLines("ar_players.txt");
            msgs = BleachFileMang.readFileLines("ar_messages.txt");
            getSetting(0).asToggle().toggle();
            BleachLogger.infoMessage("Files reloaded");
        }
    }

    private void disable(String fileName) {
        BleachLogger.errorMessage("./" + BleachHack.NAME + "/" + fileName +" is empty! Disabling AutoRespond...");
        BleachLogger.infoMessage("Check " + BleachHack.NAME + " in your Minecraft folder!");
        setToggled(false);
    }
}
