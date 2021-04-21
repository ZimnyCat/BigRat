package bleach.hack.module.mods;

import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import org.lwjgl.glfw.GLFW;

public class ColourChooser extends Module {
    public ColourChooser() {
        super("ColourChooser", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.CLIENT, "HUD color settings",
                new SettingToggle("Rainbow", false),
                new SettingSlider("Red", 0, 255, 100, 0),
                new SettingSlider("Green", 0, 255, 140, 0),
                new SettingSlider("Blue", 0, 255, 255, 0),
                new SettingSlider("TextRed", 0, 255, 100, 0),
                new SettingSlider("TextGreen", 0, 255, 255, 0),
                new SettingSlider("TextBlue", 0, 255, 255, 0));
    }

    @Override
    public void onEnable() {
        setToggled(false);
    }
}