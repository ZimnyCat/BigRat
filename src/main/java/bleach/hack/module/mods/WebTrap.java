package bleach.hack.module.mods;

import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.utils.CABlocker;
import bleach.hack.utils.CrystalUtils;
import bleach.hack.utils.Finder;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class WebTrap extends Module {

    public WebTrap() {
        super("WebTrap", KEY_UNBOUND, Category.COMBAT, "Places webs to trap and kill players with CrystalAura/AnchorAura",
                new SettingSlider("Range", 0, 8, 5, 1));
    }

    @Subscribe
    public void render(EventWorldRender rnd) {
        Integer webSlot = Finder.find(Items.COBWEB, true);
        if (webSlot == null) return;

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (mc.player.distanceTo(p) > getSetting(0).asSlider().getValue()
                    || mc.world.getBlockState(p.getBlockPos()).getBlock() == Blocks.COBWEB || mc.player == p) continue;
            BlockPos playerPos = p.getBlockPos();
            List<BlockPos> poses = Arrays.asList(
                    playerPos.north(),
                    playerPos.west(),
                    playerPos.south(),
                    playerPos.east()
            );

            for (BlockPos pos : poses) {
                if (CrystalUtils.canPlaceCrystal(pos.down())) {
                    if (!CABlocker.blocked) {
                        CABlocker.blocked = true;
                        CABlocker.blockTime = System.currentTimeMillis();
                        return;
                    }

                    Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    int preSlot = mc.player.inventory.selectedSlot;
                    mc.player.inventory.selectedSlot = webSlot;
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                            vec, Direction.DOWN, playerPos, true
                    ));
                    mc.player.swingHand(Hand.MAIN_HAND);
                    mc.player.inventory.selectedSlot = preSlot;
                }
            }
        }
    }

}
