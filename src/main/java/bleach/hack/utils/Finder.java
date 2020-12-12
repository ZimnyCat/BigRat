package bleach.hack.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;

public class Finder {
    public static Integer find(Item item, boolean hotbarOnly) {
        int num = 36;
        if (hotbarOnly) num = 9;
        Integer itemSlot = null;
        for (int slot = 0; slot < num; slot++) {
            if (MinecraftClient.getInstance().player.inventory.getStack(slot).getItem() == item) {
                itemSlot = slot;
                break;
            }
        }
        return itemSlot;
    }
}
