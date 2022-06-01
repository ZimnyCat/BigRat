package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Landing extends Module {

    public Landing() {
        super("Landing", KEY_UNBOUND, Category.WORLD, "Places blocks in the air to prevent falling",
                new SettingToggle("MLG Water Drop", false));
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

        mc.player.pitch = 90;
        double playerX = Math.floor(mc.player.getX());
        double playerZ = Math.floor(mc.player.getZ());
        mc.player.updatePosition(playerX + 0.5, mc.player.getY(), playerZ + 0.5);
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionOnly(playerX + 0.5, mc.player.getY(), playerZ + 0.5, mc.player.isOnGround()));

        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                vec, Direction.UP, block, true
        ));
        mc.player.swingHand(Hand.MAIN_HAND);

        toggle();

        if (getSetting(0).asToggle().state && mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock() != Blocks.WATER) {
            Integer sl = Finder.find(Items.WATER_BUCKET, true);
            if (sl == null) return;
            mc.player.inventory.selectedSlot = sl;
            mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
        }
    }

}
