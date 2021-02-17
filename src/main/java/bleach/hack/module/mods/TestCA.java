package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.CrystalUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class TestCA extends Module {

    public TestCA() {
        super("TestCA", KEY_UNBOUND, Category.COMBAT, "new crystal aura test",
            new SettingSlider("ExplodeRange", 1, 8, 5, 1),
            new SettingSlider("PlaceRange", 1, 8, 5, 1));
    }

    @Subscribe
    public void onTick(EventTick e) {
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof EndCrystalEntity)
                    || mc.player.distanceTo(entity) >= getSetting(0).asSlider().getValue()) continue;

            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.OFF_HAND);
        }

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (mc.player.distanceTo(p) >= 8 || p == mc.player
                    || mc.player.inventory.getMainHandStack().getItem() != Items.END_CRYSTAL) continue;

            BlockPos bp = p.getBlockPos().down();
            List<BlockPos> poses = Arrays.asList(
                    bp.add(1, 0 ,0),
                    bp.add(-1, 0 ,0),
                    bp.add(0, 0 ,1),
                    bp.add(0, 0 ,-1));

            for (BlockPos pos : poses) {
                if (pos.getSquaredDistance(mc.player.getPos(), true) >= getSetting(1).asSlider().getValue() * 10) continue;

                if (CrystalUtils.canPlaceCrystal(pos)) {
                    Vec3d posv3d = new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
                            new BlockHitResult(posv3d, Direction.UP, pos, false));
                }
            }
        }
    }
}
