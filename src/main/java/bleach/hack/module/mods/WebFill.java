package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
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


            if (placeWeb(entity.getBlockPos().down(), slot)) return;
            else if ((entity.getY() - Math.floor(entity.getY())) > 0.8) placeWeb(entity.getBlockPos(), slot);
        }
    }

    private boolean placeWeb(BlockPos pos, int slot) {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) return false;
        if (getSetting(0).asToggle().state && pos != mc.player.getBlockPos()) return false;
        Vec3d vecPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        List<BlockPos> poses = Arrays.asList(
                pos.north(),
                pos.east(),
                pos.south(),
                pos.west()
        );

        for (BlockPos p : poses) {
            if (mc.world.getBlockState(p).getBlock() != Blocks.OBSIDIAN
                    && mc.world.getBlockState(p).getBlock() != Blocks.BEDROCK) return false;
        }

        int preSlot = mc.player.inventory.selectedSlot;
        mc.player.inventory.selectedSlot = slot;
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                vecPos, Direction.DOWN, pos, true
        ));
        WorldUtils.manualAttackBlock(pos.getX(), pos.getY(), pos.getZ());
        mc.player.inventory.selectedSlot = preSlot;
        return true;
    }

}
