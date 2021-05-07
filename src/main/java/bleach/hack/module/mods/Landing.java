package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Landing extends Module {

    public Landing() {
        super("Landing", KEY_UNBOUND, Category.WORLD, "Places blocks in the air to prevent falling");
    }

    @Subscribe
    public void onTick(EventTick e) {
        BlockPos block = mc.player.getBlockPos().down().down();
        Vec3d vec = new Vec3d(block.getX(), block.getY(), block.getZ());

        if (!mc.world.getBlockState(block).isAir()) {
            BleachLogger.infoMessage("Can't place the block!");
            toggle();
            return;
        }

        if (!(mc.player.inventory.getMainHandStack().getItem() instanceof BlockItem)) {
            Integer blockSlot = null;
            for (int slot = 0; slot < 9; slot++) {
                Item item = mc.player.inventory.getStack(slot).getItem();
                if (item instanceof BlockItem) {
                    blockSlot = slot;
                    break;
                }
            }
            if (blockSlot == null) {
                BleachLogger.infoMessage("No blocks found in hotbar!");
                toggle();
                return;
            }
            mc.player.inventory.selectedSlot = blockSlot;
        }

        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                vec, Direction.UP, block, true
        ));
        WorldUtils.manualAttackBlock(block.getX(), block.getY(), block.getZ());
        toggle();
    }

}
