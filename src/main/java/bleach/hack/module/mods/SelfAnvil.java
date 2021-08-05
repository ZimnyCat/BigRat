package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import bleach.hack.utils.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SelfAnvil extends Module {

    public SelfAnvil() {
        super("SelfAnvil", KEY_UNBOUND, Category.COMBAT, "SafeHole but with anvil",
            new SettingToggle("AirPlace", true),
            new SettingToggle("Autocenter", true));
    }

    @Subscribe
    public void onTick(EventTick e) {
        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos anvil = playerPos.up().up();
        Vec3d vecPos = new Vec3d(anvil.getX(), anvil.getY(), anvil.getZ());

        if (!WorldUtils.isBlockEmpty(anvil) || !mc.world.getBlockState(playerPos).isAir()) {
            BleachLogger.infoMessage("Can't place the block!");
            toggle();
            return;
        }

        Integer slot = Finder.find(Items.ANVIL, true);
        if (slot == null) {
            BleachLogger.infoMessage("No anvils found in hotbar!");
            toggle();
            return;
        }

        if (getSetting(1).asToggle().state) {
            double playerX = Math.floor(mc.player.getX());
            double playerZ = Math.floor(mc.player.getZ());
            mc.player.updatePosition(playerX + 0.5, mc.player.getY(), playerZ + 0.5);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionOnly(playerX + 0.5, mc.player.getY(), playerZ + 0.5, mc.player.isOnGround()));
        }

        int preSlot = mc.player.inventory.selectedSlot;
        mc.player.inventory.selectedSlot = slot;
        if (getSetting(0).asToggle().state) {
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                    vecPos, Direction.DOWN, anvil, true
            ));
            WorldUtils.manualAttackBlock(anvil.getX(), anvil.getY(), anvil.getZ());
            mc.player.inventory.selectedSlot = preSlot;
            toggle();
            return;
        }
        WorldUtils.placeBlock(anvil, mc.player.inventory.selectedSlot, false, false);
        mc.player.inventory.selectedSlot = preSlot;
        toggle();
    }
}
