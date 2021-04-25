// Taken from https://github.com/Patbox/polymer/

package bleach.hack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class MixinServerPlayerInteractionManager {

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private int tickCounter;

    @Inject(method = "continueMining", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"))
    private void onUpdateBreakStatus(BlockState state, BlockPos pos, int i, CallbackInfoReturnable<Float> cir) {
        int j = tickCounter - i;
        float f = state.calcBlockBreakingDelta(this.player, this.player.world, pos) * (float)(j + 1);
        int k = (int)(f * 10.0F);

        this.player.networkHandler.sendPacket(new BlockBreakingProgressS2CPacket(-1, pos, k));
    }
}
