package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.List;
import java.util.Random;

public class AutoEZ extends Module {

    long killTime = 0;

    public AutoEZ() { super("AutoEZ", KEY_UNBOUND, Category.CHAT, "Does exactly what you think it does"); }

    @Override
    public void init() {
        super.init();
        if (!BleachFileMang.fileExists("AutoEZ.txt")) {
            BleachFileMang.createFile("AutoEZ.txt");
            BleachFileMang.appendFile("EZ! " + BleachHack.CLIENT + " on top!", "AutoEZ.txt");
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        BleachLogger.infoMessage("You can edit AutoEZ messages in " + BleachHack.NAME + "/AutoEZ.txt");
    }

    @Subscribe
    public void deathMSG(EventReadPacket event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket)) return;

        String msg = ((GameMessageS2CPacket) event.getPacket()).getMessage().getString().toLowerCase();
        KillStreak ks = new KillStreak();
        for (String word : ks.killWords) {
            if (msg.contains(word) && msg.contains(mc.player.getDisplayName().getString().toLowerCase())
                    && ((GameMessageS2CPacket) event.getPacket()).getSenderUuid().toString().contains("000000000")) {
                killTime = System.currentTimeMillis();
            }
        }
    }

    @Subscribe
    public void onTick(EventTick et) {
        if (killTime != 0 && (System.currentTimeMillis() - killTime) > 200 && !mc.player.isDead()) {
            Random r = new Random();
            List<String> lines = BleachFileMang.readFileLines("AutoEZ.txt");
            if (lines.isEmpty()) mc.player.sendChatMessage("EZ! " + BleachHack.CLIENT + " on top!");
            else mc.player.sendChatMessage(lines.get(r.nextInt(lines.size())));
            killTime = 0;
        }

        if (mc.player.isDead()) {
            killTime = 0;
        }
    }
}
