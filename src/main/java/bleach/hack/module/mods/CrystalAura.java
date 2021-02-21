package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.CrystalUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class CrystalAura extends Module {

    List<String> coords = new ArrayList<>();
    HashMap<BlockPos, BlockPos> poses = new HashMap<>();

    public CrystalAura() {
        super("CrystalAura", KEY_UNBOUND, Category.COMBAT, "Does exactly what you think it does",
            new SettingSlider("ExplodeRange", 1, 8, 5, 1),
            new SettingSlider("PlaceRange", 1, 8, 5, 1),
            new SettingToggle("OnlyOwn", true),
            new SettingToggle("AutoPlace", true).withDesc("also known as AutoSuicide"),
            new SettingToggle("1.13+ place", false));
    }

    @Subscribe
    public void onTick(EventTick e) {
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof EndCrystalEntity)
                    || mc.player.distanceTo(entity) >= getSetting(0).asSlider().getValue()) continue;
            BlockPos crystalPos = entity.getBlockPos();
            if (coords.contains(crystalPos.getX() + " " + crystalPos.getY() + " " + crystalPos.getZ())
                    || !getSetting(2).asToggle().state) {
                mc.interactionManager.attackEntity(mc.player, entity);
                mc.player.swingHand(Hand.OFF_HAND);
                coords.remove(crystalPos.getX() + " " + crystalPos.getY() + " " + crystalPos.getZ());
                break;
            }
        }

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (!getSetting(3).asToggle().state) break;

            if (mc.player.distanceTo(p) >= 8 || p == mc.player
                    || mc.player.inventory.getMainHandStack().getItem() != Items.END_CRYSTAL) continue;

            BlockPos bp = p.getBlockPos().down();
            poses.put(bp.add(1, 0 ,0), bp.add(2, 0 ,0));
            poses.put(bp.add(-1, 0 ,0), bp.add(-2, 0 ,0));
            poses.put(bp.add(0, 0 ,1), bp.add(0, 0 ,2));
            poses.put(bp.add(0, 0 ,-1), bp.add(0, 0 ,-2));
            for (Map.Entry nigg : poses.entrySet()) {

                BlockPos pos1 = (BlockPos) nigg.getKey();
                BlockPos pos2 = (BlockPos) nigg.getValue();

                if (CrystalUtils.canPlaceCrystal(pos1)) {
                    if (!place(pos1)) continue;
                    break;
                } else if (mc.world.getBlockState(pos1.up()).getBlock() == Blocks.AIR && CrystalUtils.canPlaceCrystal(pos2)) {
                    if (!place(pos2)) continue;
                    break;
                }
            }
            poses.clear();
        }
    }

    private boolean place(BlockPos pos) {
        if (pos.getSquaredDistance(mc.player.getPos(), true) >= getSetting(1).asSlider().getValue() * 3) return false;
        Vec3d posv3d = new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
                new BlockHitResult(posv3d, Direction.UP, pos, false));
        if (getSetting(2).asToggle().state) coords.add(pos.up().getX() + " " + pos.up().getY() + " " + pos.up().getZ());
        return true;
    }
}
