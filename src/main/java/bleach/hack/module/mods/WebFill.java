package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import bleach.hack.utils.WorldUtils;
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

import java.util.Arrays;
import java.util.List;

public class WebFill extends Module {

    public WebFill() {
        super("WebFill", KEY_UNBOUND, Category.COMBAT, "Fills holes when another player is near them",
                new SettingToggle("OnlyOwn", false));
    }

    @Subscribe
    public void onTick(EventTick e) {
        Integer slot = Finder.find(Items.COBWEB, true);
        if (slot == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof PlayerEntity) || entity == mc.player || mc.player.distanceTo(entity) > 6
                    || BleachHack.friendMang.has(entity.getDisplayName().getString())) continue;

            BlockPos playerPosDown = entity.getBlockPos().down();
            if (mc.world.getBlockState(playerPosDown).getBlock() != Blocks.AIR) return;
            if (getSetting(0).asToggle().state && playerPosDown != mc.player.getBlockPos()) return;
            Vec3d vecPos = new Vec3d(playerPosDown.getX(), playerPosDown.getY(), playerPosDown.getZ());
            List<BlockPos> poses = Arrays.asList(
                    playerPosDown.north(),
                    playerPosDown.east(),
                    playerPosDown.south(),
                    playerPosDown.west()
            );

            boolean con = false;
            for (BlockPos pos : poses) {
                if (mc.world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN
                        && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) con = true;
            } if (con) continue;

            int preSlot = mc.player.inventory.selectedSlot;
            mc.player.inventory.selectedSlot = slot;
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                    vecPos, Direction.DOWN, playerPosDown, true
            ));
            WorldUtils.manualAttackBlock(playerPosDown.getX(), playerPosDown.getY(), playerPosDown.getZ());
            mc.player.inventory.selectedSlot = preSlot;
        }

    }

}
