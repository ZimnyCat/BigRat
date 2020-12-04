package bleach.hack.module.mods;

import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class BlockHighlight extends Module {
    public BlockHighlight() { super("BlockHighlight", KEY_UNBOUND, Category.RENDER, "Highlights blocks you are looking at"); }

    @Subscribe
    public void onDraw(EventWorldRender e) {
        if (mc.crosshairTarget == null || !(mc.crosshairTarget instanceof BlockHitResult)) return;
        BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
        if (mc.world.isAir(pos)) return;
        RenderUtils.drawFilledBox(pos, 200, 100, 255, 1F);
    }
}
