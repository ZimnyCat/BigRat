package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import bleach.hack.utils.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class SafeHole extends Module {

    public SafeHole() {
        super("SafeHole", KEY_UNBOUND, Category.COMBAT, "Makes your hole 100% safe by placing obsidian over you",
                new SettingToggle("AirPlace", true),
                new SettingToggle("Keep on", false));
    }

    @Subscribe
    public void onTick(EventTick e) {
        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos obsidian = playerPos.up().up();
        Vec3d vecPos = new Vec3d(obsidian.getX(), obsidian.getY(), obsidian.getZ());

        if (!WorldUtils.isBlockEmpty(obsidian)) {
            if (!getSetting(1).asToggle().state) {
                BleachLogger.infoMessage("Can't place the block!");
                toggle();
            }
            return;
        }

        List<BlockPos> poses = Arrays.asList(
                playerPos.north(),
                playerPos.east(),
                playerPos.south(),
                playerPos.west()
        );

        for (BlockPos pos : poses) {
            Block block = mc.world.getBlockState(pos).getBlock();
            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN) return;
        }

        Integer slot = Finder.find(Items.OBSIDIAN, true);
        if (slot == null) {
            if (!getSetting(1).asToggle().state) {
                BleachLogger.infoMessage("No obsidian found in hotbar!");
                toggle();
            }
            return;
        }
        int preSlot = mc.player.inventory.selectedSlot;
        mc.player.inventory.selectedSlot = slot;
        if (getSetting(0).asToggle().state) {
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                    vecPos, Direction.DOWN, obsidian, true
            ));
            WorldUtils.manualAttackBlock(obsidian.getX(), obsidian.getY(), obsidian.getZ());
            mc.player.inventory.selectedSlot = preSlot;
            if (!getSetting(1).asToggle().state) toggle();
            return;
        }
        WorldUtils.placeBlock(obsidian, mc.player.inventory.selectedSlot, false, false);
        mc.player.inventory.selectedSlot = preSlot;

        if (!getSetting(1).asToggle().state) toggle();
    }

}
