package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventSendPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.FriendManager;
import bleach.hack.utils.WorldUtils;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BedBomb extends Module {

    public BedBomb() {
        super("BedBomb", KEY_UNBOUND, Category.COMBAT, "beds go boom",
                new SettingToggle("AutoReplace", true).withDesc("Replaces beds in hotbar").withChildren(
                        new SettingSlider("MainBedSlot", 1, 9, 2, 0)
                ),
                new SettingToggle("OsamaBedLaden", true),
                new SettingToggle("AttackOnly", false),
                new SettingSlider("AttackRange", 3, 12, 8, 1));
    }
    @BleachSubscribe
    public void onTick(EventTick event) {
        Integer mainBedSlot = (int)getSetting(0).asToggle().getChild(0).asSlider().getValue();
        if (!(mc.player.getInventory().getStack(mainBedSlot - 1).getItem() instanceof BedItem)
                && !mc.player.isCreative()
                && dimensionCheck()
                && getSetting(0).asToggle().state
                && (checkAttackRange() || !getSetting(2).asToggle().state)) {
            Integer bedSlot = null;
            for (int slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.getInventory().getStack(slot);
                if (stack.getItem() instanceof BedItem) bedSlot = slot;
            }
            if (bedSlot == null || bedSlot == mainBedSlot - 1) return;
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bedSlot < 9 ? (bedSlot + 36) : (bedSlot), 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);

        }
    }
    @BleachSubscribe
    public void allahuAkbar(EventWorldRender worldRender) {
        if ((getSetting(1).asToggle().state && dimensionCheck()) && (checkAttackRange() || !getSetting(2).asToggle().state)
                && !mc.player.isSneaking()) {
            for (BlockEntity e : WorldUtils.getBlockEntities()) {
                if (e instanceof BedBlockEntity && e.getPos().getSquaredDistance(mc.player.getPos(), true) < 30) {
                    BlockPos pos = e.getPos();
                    Vec3d posv3d = new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(posv3d, Direction.UP, pos, false));
                }
            }
        }
    }

    @BleachSubscribe
    public void onInteract(EventSendPacket e) {
        if (!(e.getPacket() instanceof PlayerInteractBlockC2SPacket)) return;
        e.setCancelled(getSetting(2).asToggle().state && lookingOnBed() && !checkAttackRange() && dimensionCheck());
    }

    public boolean dimensionCheck() {
        return mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether")
                || mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_end");
    }

    private boolean checkAttackRange() {
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof PlayerEntity) || e == mc.player || ((PlayerEntity) e).isDead()) continue;
            if (BleachHack.friendMang.has(e.getDisplayName().getString())) continue;
            if (mc.player.distanceTo(e) <= getSetting(3).asSlider().getValue()) return true;
        }
        return false;
    }

    private boolean lookingOnBed() {
        return mc.crosshairTarget instanceof BlockHitResult
                && mc.world.getBlockEntity(((BlockHitResult) mc.crosshairTarget).getBlockPos()) instanceof BedBlockEntity;
    }
}
