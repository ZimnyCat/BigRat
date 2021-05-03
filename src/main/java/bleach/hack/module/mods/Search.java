package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.utils.RenderUtils;
import bleach.hack.utils.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Search extends Module {
    
    // ------
    // МОДУЛЬ ХУЙНЯ И ЛАГАЕТ
    // НЕ ИСПОЛЬЗУЙТЕ ЭТОТ КОД
    // ------

    long time = 0;
    List<Block> blocks = new ArrayList<>();
    List<BlockPos> blockPoses = new ArrayList<>();

    public Search() {
        super("Search", KEY_UNBOUND, Category.RENDER, "Highlights selected blocks",
                new SettingSlider("R", 1, 255, 100, 0),
                new SettingSlider("G", 1, 255, 255, 0),
                new SettingSlider("B", 1, 255, 255, 0),
                new SettingSlider("A", 1, 100, 100, 0),
                new SettingSlider("Range", 5, 100, 30, 0));
    }

    @Override
    public void init() {
        super.init();
        BleachFileMang.createFile("SearchBlocks.txt");
    }

    @Subscribe
    public void onTick(EventTick event) {
        if ((System.currentTimeMillis() - time) > 1000) {
            blocks.clear();
            BleachFileMang.readFileLines("SearchBlocks.txt").forEach(line -> {
                try {
                    blocks.add(Registry.BLOCK.get(new Identifier(line)));
                } catch (Exception e) {
                    System.out.println("Invalid block name! (" + line + ")");
                }
            });
        }

        getBlocks((int) getSetting(4).asSlider().getValue());
    }


    @Subscribe
    public void renderBlocks(EventWorldRender e) {
        for (BlockPos pos : blockPoses) RenderUtils.drawFilledBox(pos,
                (float) getSetting(0).asSlider().getValue() / 255,
                (float) getSetting(1).asSlider().getValue() / 255,
                (float) getSetting(2).asSlider().getValue() / 255,
                (float) getSetting(3).asSlider().getValue() / 100);
    }

    public void getBlocks(int range) {
        blockPoses.clear();
        BlockPos player = mc.player.getBlockPos();

        for (int y = -Math.min(range, player.getY()); y < Math.min(range, 255 - player.getY()); ++y) {
            for (int x = -range; x < range; ++x) {
                for (int z = -range; z < range; ++z) {
                    BlockPos pos = player.add(x, y, z);
                    for (Block block : blocks) {
                        if (block == mc.world.getBlockState(pos).getBlock()) blockPoses.add(pos);
                    }
                }
            }
        }
    }

}
