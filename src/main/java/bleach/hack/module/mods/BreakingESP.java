package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class BreakingESP extends Module {

    HashMap<BlockPos, Long> blockTime = new HashMap<>();
    HashMap<BlockPos, Integer> blocks = new HashMap<>();

    public BreakingESP() {
        super("BreakingESP", KEY_UNBOUND, Category.RENDER, "Highlights blocks being broken near you");
    }

    @Subscribe
    public void breaking(EventReadPacket event) {
        if (!(event.getPacket() instanceof BlockBreakingProgressS2CPacket)) return;
        BlockBreakingProgressS2CPacket packet = (BlockBreakingProgressS2CPacket) event.getPacket();

        try {
            blocks.forEach((pos, progress) -> {
                if ((System.currentTimeMillis() - blockTime.get(pos)) > 100) {
                    blocks.remove(pos);
                }
            });
        }
        // https://i.imgur.com/0WzWp6i.png
        catch (Exception ignored) { }

        blocks.put(packet.getPos(), packet.getProgress());
        blockTime.put(packet.getPos(), System.currentTimeMillis());
    }

    @Subscribe
    public void worldRender(EventWorldRender event) {
        try {
            blocks.forEach((pos, progress) -> {
                RenderUtils.drawFilledBox(pos, 1f, 1f/255, 1f/255, 100f/255);
                if (progress == 0 || mc.world.getBlockState(pos).isAir()) blocks.remove(pos);
            });
        }
        // https://i.imgur.com/0WzWp6i.png
        catch (Exception ignored) { }
    }
}
