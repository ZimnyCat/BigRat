package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class JoinLeaveMSGs extends Module {

    List<String> players = new ArrayList<>();
    long time = -1;

    public JoinLeaveMSGs() {
        super("JoinLeaveMSGs", KEY_UNBOUND, Category.CHAT, "Notifies when someone joins/leaves",
                new SettingMode("Style", "Vanilla", "Old 2b2t"),
                new SettingToggle("Announce", false));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (time == -1) {
            for (PlayerListEntry player : mc.player.networkHandler.getPlayerList())
                players.add(player.getProfile().getName());
            time = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - time > 1000) {
            for (PlayerListEntry player : mc.player.networkHandler.getPlayerList()) {
                if (!players.contains(player.getProfile().getName()) && !players.isEmpty()) {
                    BleachLogger.noPrefixMessage(getMSG(player.getProfile().getName() + " joined"));
                    if (getSetting(1).asToggle().state) mc.player.sendChatMessage("hi " + player.getProfile().getName());
                }
            }
            for (String player : players) {
                if (mc.player.networkHandler.getPlayerListEntry(player) == null) {
                    BleachLogger.noPrefixMessage(getMSG(player + " left"));
                    if (getSetting(1).asToggle().state) mc.player.sendChatMessage("goodbye " + player);
                }
            }
            time = -1;
            players.clear();
        }
    }

    @Subscribe
    public void join(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameJoinS2CPacket)) return;
        players.clear();
    }

    private String getMSG(String name) {
        return getSetting(0).asMode().mode == 0 ? Formatting.YELLOW + name + " the game" : Formatting.GRAY + name;
    }
}
