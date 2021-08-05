package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.utils.RenderUtils;
import bleach.hack.utils.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Search extends Module {

    long time = 0;
    long time2 = 0;
    List<Block> blocks = new ArrayList<>();
    List<BlockPos> blockPoses = new ArrayList<>();

    public Search() {
        super("Search", KEY_UNBOUND, Category.RENDER, "Highlights selected blocks in the world",
                new SettingSlider("R", 1, 255, 100, 0),
                new SettingSlider("G", 1, 255, 255, 0),
                new SettingSlider("B", 1, 255, 255, 0),
                new SettingSlider("A", 1, 100, 100, 0),
                new SettingSlider("Range", 5, 100, 30, 0),
                new SettingToggle("Tracers", true));
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
            BleachFileMang.readFileLines("SearchBlocks.txt").forEach(line -> blocks.add(Registry.BLOCK.get(new Identifier(line))));
            time = System.currentTimeMillis();
        }

        getBlocks((int) getSetting(4).asSlider().getValue());
    }


    @Subscribe
    public void renderBlocks(EventWorldRender e) {
        for (BlockPos pos : blockPoses) {
            RenderUtils.drawFilledBox(pos,
                    (float) getSetting(0).asSlider().getValue() / 255,
                    (float) getSetting(1).asSlider().getValue() / 255,
                    (float) getSetting(2).asSlider().getValue() / 255,
                    (float) getSetting(3).asSlider().getValue() / 100);

            if (getSetting(5).asToggle().state) {
                Vec3d cum = new Vec3d(0, 0, 75).rotateX(-(float) Math.toRadians(mc.cameraEntity.pitch))
                        .rotateY(-(float) Math.toRadians(mc.cameraEntity.yaw))
                        .add(mc.cameraEntity.getPos().add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));

                RenderUtils.drawLine(cum.x, cum.y, cum.z, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        (float) getSetting(0).asSlider().getValue() / 255,
                        (float) getSetting(1).asSlider().getValue() / 255,
                        (float) getSetting(2).asSlider().getValue() / 255, 1);
            }
        }
    }

    public void getBlocks(int range) {
        if ((System.currentTimeMillis() - time2) > 2000) {
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
            time2 = System.currentTimeMillis();
        }
    }

}
