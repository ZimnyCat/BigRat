package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;


public class HoleTP extends Module {

    BlockPos currentHole;

    public HoleTP() { super("HoleTP", KEY_UNBOUND, Category.MOVEMENT, "Crystal PvP Holes HATE This Module..."); }

    @Subscribe
    public void onTick(EventTick event) {
        BlockPos playerPos = mc.player.getBlockPos();

        if (isHole(playerPos.down()) && !playerPos.down().equals(currentHole)) {
            double playerX = Math.floor(mc.player.getX());
            double playerZ = Math.floor(mc.player.getZ());
            mc.player.updatePosition(playerX + 0.5, mc.player.getY() - 0.1, playerZ + 0.5);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionOnly(playerX + 0.5, mc.player.getY() - 0.1, playerZ + 0.5, mc.player.isOnGround()));
            currentHole = playerPos.down();
            if (mc.player.stepHeight != 0) mc.player.stepHeight = 0;
        }

        if (!playerPos.equals(currentHole) && !playerPos.down().equals(currentHole)) {
            mc.player.stepHeight = 0.5f;
            currentHole = null;
        }
    }

    private boolean isHole(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) return false;

        List<BlockPos> poses = Arrays.asList(
                pos.north(),
                pos.east(),
                pos.south(),
                pos.west(),
                pos.down()
        );

        for (BlockPos p : poses) {
            Block b = mc.world.getBlockState(p).getBlock();
            if (b != Blocks.OBSIDIAN && b != Blocks.BEDROCK) return false;
        }

        return true;
    }
}
