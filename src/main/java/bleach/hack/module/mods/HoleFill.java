package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.CABlocker;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoleFill extends Module {

    long last = 0;

    public HoleFill() {
        super("HoleFill", KEY_UNBOUND, Category.COMBAT, "Fills holes when another player is near them",
                new SettingMode("Block", "Obsidian", "Web"),
                new SettingSlider("Delay", 0, 4000, 1000, 0));
    }

    @Subscribe
    public void onTick(EventTick e) {
        if ((System.currentTimeMillis() - last) < getSetting(1).asSlider().getValue()) return;

        Integer slot = Finder.find((getSetting(0).asMode().mode == 0 ? Items.OBSIDIAN : Items.COBWEB), true);
        if (slot == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof PlayerEntity) || entity == mc.player || mc.player.distanceTo(entity) > 6
                    || BleachHack.friendMang.has(entity.getDisplayName().getString())) continue;

            BlockPos bp = entity.getBlockPos();
            List<BlockPos> bpList = new ArrayList<>();

            for (int y = -Math.min(3, bp.getY()); y < Math.min(3, 255 - bp.getY()); ++y) {
                for (int x = -3; x < 3; ++x) {
                    for (int z = -3; z < 3; ++z) {
                        BlockPos pos = bp.add(x, y, z);
                        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                            bpList.add(pos);
                        }
                    }
                }
            }

            for (BlockPos blockPos : bpList) {
                if (!isSurrounded(blockPos)
                        || Math.sqrt(mc.player.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ())) > 4)
                    continue;

                if (!CABlocker.blocked) {
                    CABlocker.blocked = true;
                    CABlocker.blockTime = System.currentTimeMillis();
                }

                int prevSlot = mc.player.inventory.selectedSlot;
                mc.player.inventory.selectedSlot = slot;
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.DOWN, blockPos, true
                ));
                mc.player.swingHand(Hand.MAIN_HAND);
                mc.player.inventory.selectedSlot = prevSlot;
                last = System.currentTimeMillis();
                return;
            }
        }
    }

    private boolean isSurrounded(BlockPos bpos) {
        List<BlockPos> posesAround = Arrays.asList(
                bpos.north(),
                bpos.east(),
                bpos.south(),
                bpos.west(),
                bpos.down()
        );

        for (BlockPos p : posesAround) {
            if (mc.world.getBlockState(p).getBlock() != Blocks.OBSIDIAN
                    && mc.world.getBlockState(p).getBlock() != Blocks.BEDROCK) return false;
        }
        return true;
    }
}
