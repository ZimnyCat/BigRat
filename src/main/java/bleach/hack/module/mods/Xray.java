/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack.module.mods;

import bleach.hack.event.events.EventBlockRender;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class Xray extends Module {

    private final Set<Block> visibleBlocks = new HashSet<>();
    private List<String> defaults = Arrays.asList(
            "coal_ore",
            "iron_ore",
            "diamond_ore"
    );
    private double gamma;

    public Xray() {
        super("Xray", KEY_UNBOUND, Category.RENDER, "Baritone is for zoomers",
                new SettingToggle("Fluids", true));
    }

    public boolean isVisible(Block block) {
        return !this.isToggled() || this.visibleBlocks.contains(block);
    }

    public void setVisible(Block... blocks) {
        Collections.addAll(this.visibleBlocks, blocks);
    }

    public void setInvisible(Block... blocks) {
        this.visibleBlocks.removeAll(Arrays.asList(blocks));
    }

    public Set<Block> getVisibleBlocks() {
        return visibleBlocks;
    }

    @Override
    public void onEnable() {
        BleachFileMang.createEmptyFile("xrayblocks.txt");
        defaults.forEach(blockName -> BleachFileMang.appendFile(blockName, "xrayblocks.txt"));

        if (mc.world == null) return;
        visibleBlocks.clear();

        BleachFileMang.readFileLines("xrayblocks.txt").forEach(s -> setVisible(Registry.BLOCK.get(new Identifier(s))));
        mc.worldRenderer.reload();
        gamma = mc.options.gamma;

        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mc.world != null) mc.worldRenderer.setWorld(mc.world);

        mc.options.gamma = gamma;

        mc.worldRenderer.reload();

        super.onDisable();
    }

    @Subscribe
    public void blockRender(EventBlockRender eventBlockRender) {
        if (this.isVisible(eventBlockRender.getBlockState().getBlock())) {
            eventBlockRender.setCancelled(true);
        }
    }

    @Subscribe
    public void onTick(EventTick eventPreUpdate) {
        mc.options.gamma = 69.420;
    }

}
