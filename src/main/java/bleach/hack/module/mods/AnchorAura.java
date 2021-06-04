package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.Finder;
import bleach.hack.utils.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AnchorAura extends Module {

    List<BlockPos> anchors = new ArrayList<>();
    int ticks = 0;

    public AnchorAura() {
        super("AnchorAura", KEY_UNBOUND, Category.COMBAT, "Places respawn anchors to kill players",
                new SettingSlider("Range", 1, 8, 5, 1),
                new SettingToggle("AutoPlace", true),
                new SettingToggle("Mine", true),
                new SettingToggle("AntiSuicide", true),
                new SettingSlider("Delay", 0, 4, 0, 0));
    }

    @Subscribe
    public void worldRender(EventWorldRender e) {
        if (!mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("overworld")) return;
        Integer raSlot = Finder.find(Items.RESPAWN_ANCHOR, true);
        Integer gsSlot = Finder.find(Items.GLOWSTONE, true);
        if (raSlot == null || gsSlot == null) return;
        if (!anchors.isEmpty()) {
            for (BlockPos pos : anchors) {
                Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                if (mc.player.getBlockPos().equals(pos.down().down()) && getSetting(3).asToggle().state) continue;
                mc.player.inventory.selectedSlot = gsSlot;
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        vec, Direction.DOWN, pos, true
                ));
                mc.player.inventory.selectedSlot = raSlot;
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        vec, Direction.DOWN, pos, true
                ));
            }
            anchors.clear();
        }

        if (getSetting(1).asToggle().state) {
            int range = (int) getSetting(0).asSlider().getValue();
            for (PlayerEntity p : mc.world.getPlayers()) {
                if (mc.player.getBlockPos().equals(p.getBlockPos()) && getSetting(3).asToggle().state) continue;
                if (mc.player.distanceTo(p) > range || p == mc.player || p.isDead()
                        || BleachHack.friendMang.has(p.getDisplayName().getString())) continue;
                BlockPos pos = p.getBlockPos().up().up();
                Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                if (mc.world.getBlockState(pos).getBlock() == Blocks.GLOWSTONE && getSetting(2).asToggle().state) {
                    Integer pickaxe = null;
                    for (int slot = 0; slot < 9; slot++) {
                        ItemStack stack = mc.player.inventory.getStack(slot);
                        if (stack.getItem() instanceof PickaxeItem) pickaxe = slot;
                    } if (pickaxe == null) return;
                    mc.player.inventory.selectedSlot = pickaxe;

                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
                    mc.player.swingHand(Hand.MAIN_HAND);
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
                    return;
                }
                if (WorldUtils.isBlockEmpty(pos)) {
                    mc.player.inventory.selectedSlot = raSlot;
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                            vec, Direction.DOWN, pos, true
                    ));
                    WorldUtils.manualAttackBlock(pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
    }

    @Subscribe
    public void onTick(EventTick e) {
        if (ticks < (getSetting(4).asSlider().getValue() + 1)) {
            ticks++;
            return;
        }
        else ticks = 0;

        int range = (int) getSetting(0).asSlider().getValue();
        BlockPos player = mc.player.getBlockPos();
        for (int y = -Math.min(range, player.getY()); y < Math.min(range, 255 - player.getY()); ++y) {
            for (int x = -range; x < range; ++x) {
                for (int z = -range; z < range; ++z) {
                    BlockPos pos = player.add(x, y, z);
                    if (mc.world.getBlockState(pos).getBlock() == Blocks.RESPAWN_ANCHOR) anchors.add(pos);
                }
            }
        }
    }
}
