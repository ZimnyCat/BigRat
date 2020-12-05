package bleach.hack.mixin;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ExtraTab;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public <E> List<E> subList(List<E> list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, ModuleManager.getModule(ExtraTab.class).isToggled() ? list.size() : toIndex);
    }
}
