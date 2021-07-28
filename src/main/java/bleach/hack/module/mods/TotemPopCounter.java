package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

import java.util.HashMap;

public class TotemPopCounter extends Module {

    HashMap<String, Integer> pops = new HashMap();

    public TotemPopCounter() {
        super("TotemPopCounter", KEY_UNBOUND, Category.COMBAT, "Counts totem pops",
                new SettingToggle("Chat", false).withChildren(
                        new SettingToggle("BigRat", true)
                ),
                new SettingToggle("Clear", false),
                new SettingSlider("Range", 2, 50, 30, 1));
    }

    @BleachSubscribe
    public void check(EventTick et) {
        if (getSetting(1).asToggle().state) {
            pops.clear();
            getSetting(1).asToggle().toggle();
        }

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p.isDead() && pops.containsKey(p.getDisplayName().getString())
                    && mc.player.distanceTo(p) < getSetting(2).asSlider().getValue()) {
                String name = p.getDisplayName().getString();
                int popNum = pops.get(name);
                BleachLogger.infoMessage("\u00a73" + name + " \u00a7fpopped \u00a73" + popNum + " \u00a7ftotems and died");
                chatMsg(name + " popped " + popNum + " totems and died", name);
                pops.remove(name);
            }
        }
    }

    @BleachSubscribe
    public void pop(EventReadPacket e) {
        if (e.getPacket() instanceof EntityStatusS2CPacket) {
            EntityStatusS2CPacket p = (EntityStatusS2CPacket) e.getPacket();
            if (!(p.getEntity(mc.world) instanceof PlayerEntity)
                    && mc.player.distanceTo(p.getEntity(mc.world)) >= getSetting(2).asSlider().getValue()) return;
            if (p.getStatus() == 35) {
                String name = p.getEntity(mc.world).getDisplayName().getString();
                int popNum = pops.containsKey(name) ? pops.get(name) + 1 : 1;
                pops.put(name, popNum);
                BleachLogger.infoMessage("\u00a73" + name + " \u00a7fpopped \u00a73" + popNum + " \u00a7ftotem" + (popNum != 1 ? "s" : ""));
                chatMsg(name + " popped " + popNum + " totem" + (popNum != 1 ? "s" : ""), name);
            }
        }
        if (e.getPacket() instanceof GameJoinS2CPacket) pops.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        pops.clear();
    }

    private void chatMsg(String msg, String name) {
        if (!getSetting(0).asToggle().state || name.equals(mc.player.getDisplayName().getString())) return;
        mc.player.sendChatMessage(msg + (getSetting(0).asToggle().getChild(0).asToggle().state ? " BigRat on top!" : ""));
    }
}
