package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class Yaw extends Module {

    private boolean lDown = false;
    private boolean uDown = false;
    private boolean rDown = false;
    private boolean dDown = false;

    public Yaw() {
        super("Yaw", KEY_UNBOUND, Category.PLAYER, "Snaps your rotations to angles",
                new SettingToggle("Yaw", true).withDesc("Fixes your yaw").withChildren(
                        new SettingMode("Interval", "45", "30", "15", "90").withDesc("What angles to snap to")),
                new SettingToggle("Pitch", false).withDesc("Fixes your pitch").withChildren(
                        new SettingMode("Interval", "45", "30", "15", "90").withDesc("What angles to snap to")),
                new SettingToggle("Arrow Move", false).withDesc("Allows you to move between angles by using your arrow keys"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        /* yes looks like a good way to do it to me */
        if (getSetting(2).asToggle().state && mc.currentScreen == null) {
            int ymode = getSetting(0).asToggle().getChild(0).asMode().mode;
            int pmode = getSetting(1).asToggle().getChild(0).asMode().mode;

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT) && !lDown) {
                mc.player.getYaw() -= ymode == 0 ? 45 : ymode == 1 ? 30 : ymode == 2 ? 15 : 90;
                lDown = true;
            } else if (!InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT)) {
                lDown = false;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT) && !rDown) {
                mc.player.getYaw() += ymode == 0 ? 45 : ymode == 1 ? 30 : ymode == 2 ? 15 : 90;
                rDown = true;
            } else if (!InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT)) {
                rDown = false;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_UP) && !uDown) {
                mc.player.getPitch() = MathHelper.clamp(mc.player.getPitch() - (pmode == 0 ? 45 : pmode == 1 ? 30 : pmode == 2 ? 15 : 90), -90, 90);
                uDown = true;
            } else if (!InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_UP)) {
                uDown = false;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_DOWN) && !dDown) {
                mc.player.getPitch() = MathHelper.clamp(mc.player.getPitch() + (pmode == 0 ? 45 : pmode == 1 ? 30 : pmode == 2 ? 15 : 90), -90, 90);
                dDown = true;
            } else if (!InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_DOWN)) {
                dDown = false;
            }
        }

        snap();
    }

    public void snap() {
        // quic maff
        if (getSetting(0).asToggle().state) {
            int mode = getSetting(0).asToggle().getChild(0).asMode().mode;
            int interval = mode == 0 ? 45 : mode == 1 ? 30 : mode == 2 ? 15 : 90;
            int rot = (int) mc.player.getYaw() + (Math.floorMod((int) mc.player.getYaw(), interval) < interval / 2 ?
                    -Math.floorMod((int) mc.player.getYaw(), interval) : interval - Math.floorMod((int) mc.player.getYaw(), interval));

            mc.player.getYaw() = rot;
        }

        if (getSetting(1).asToggle().state) {
            int mode = getSetting(1).asToggle().getChild(0).asMode().mode;
            int interval = mode == 0 ? 45 : mode == 1 ? 30 : mode == 2 ? 15 : 90;
            int rot = MathHelper.clamp(((int) mc.player.getPitch() + (Math.floorMod((int) mc.player.getPitch(), interval) < interval / 2 ?
                    -Math.floorMod((int) mc.player.getPitch(), interval) : interval - Math.floorMod((int) mc.player.getPitch(), interval))), -90, 90);

            mc.player.getPitch() = rot;
        }
    }
}