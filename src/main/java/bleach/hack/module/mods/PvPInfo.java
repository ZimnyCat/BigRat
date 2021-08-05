package bleach.hack.module.mods;

import bleach.hack.event.events.EventDrawOverlay;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PvPInfo extends Module {

    int textHeight = 0;

    public PvPInfo() {
        super("PvPInfo", KEY_UNBOUND, Category.COMBAT, "Shows pvp information about players in range",
                new SettingSlider("Range", 1, 100, 50, 1), // 0
                new SettingSlider("Height", 1, 500, 2, 1), // 1
                new SettingSlider("Width", 1, 1000, 150, 1), // 2
                new SettingToggle("HP", true), // 3
                new SettingToggle("Ping", true), // 4
                new SettingToggle("Distance", true), // 5
                new SettingToggle("Pops", true), // 6
                new SettingToggle("ProtectionSum", true), // 7
                new SettingToggle("Sharpness", true)); // 8
    }

    @Subscribe
    public void onDraw(EventDrawOverlay e) {
        List<AbstractClientPlayerEntity> players = new ArrayList<>();
        for (Entity p : mc.world.getPlayers().stream().sorted(Comparator.comparingDouble(a -> mc.player.getPos().distanceTo(a.getPos()))).collect(Collectors.toList())) {
            if (p == mc.player || mc.player.distanceTo(p) > getSetting(0).asSlider().getValue()) continue;
            players.add((AbstractClientPlayerEntity) p);
        }
        if (players.isEmpty()) return;
        for (AbstractClientPlayerEntity p : players) {
            int ping;
            TotemPopCounter tpc = (TotemPopCounter) ModuleManager.getModule(TotemPopCounter.class);
            String pops = tpc.isToggled() ? String.valueOf(tpc.pops.get(p.getDisplayName().getString()))
                    : "TotemPopCounter is disabled";
            try { ping = mc.player.networkHandler.getPlayerListEntry(p.getUuid()).getLatency(); }
            catch (Exception exception) { ping = -1; }
            List<String> info = new ArrayList<>();
            info.add("\u00a73" + p.getDisplayName().getString());
            if (getSetting(3).asToggle().state) info.add(" \u00a7fHP [\u00a73" + Math.round(p.getHealth() + p.getAbsorptionAmount()) + "\u00a7f]");
            if (getSetting(4).asToggle().state) info.add(" \u00a7fPing [\u00a73" + ping + "\u00a7f]");
            if (getSetting(5).asToggle().state) info.add(" \u00a7fDistance [\u00a73" + Math.round(mc.player.distanceTo(p)) + "\u00a7f]");
            if (getSetting(6).asToggle().state) info.add(" \u00a7fPops [\u00a73" + pops + "\u00a7f]");
            if (getSetting(7).asToggle().state) info.add(" \u00a7fProtSum [\u00a73" + protectionSum(p) + "\u00a7f]");
            if (getSetting(8).asToggle().state) info.add(" \u00a7fSharpness [\u00a73" + sharpness(p) + "\u00a7f]");
            for (String s : info) {
                mc.textRenderer.drawWithShadow(e.matrix, s, (float) getSetting(2).asSlider().getValue(),
                        (float) getSetting(1).asSlider().getValue() + textHeight, 0xa0a0a0);
                textHeight += 10;
            }
        }
        textHeight = 0;
    }

    private int protectionSum(PlayerEntity e) {
        int sum = 0;
        for (ItemStack item : e.getArmorItems()) {
            for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(item).entrySet()) {
                if (ench.getKey() == Enchantments.PROTECTION) sum += ench.getValue();
            }
        }
        return sum;
    }

    private int sharpness(PlayerEntity e) {
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(e.getMainHandStack()).entrySet()) {
            if (ench.getKey() == Enchantments.SHARPNESS) return ench.getValue();
        }
        return 0;
    }
}
