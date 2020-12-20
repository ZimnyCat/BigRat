package bleach.hack.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;

public class Finder {
    public static Integer find(Item item, boolean hotbarOnly) {
        int num = hotbarOnly ? 9 : 36;
        Integer itemSlot = null;
        for (int slot = 0; slot < num; slot++) {
            if (MinecraftClient.getInstance().player.inventory.getStack(slot).getItem() == item) {
                itemSlot = slot;
                break;
            }
        }
        if (MinecraftClient.getInstance().player.inventory.getStack(45).getItem() == item
                && itemSlot == null && !hotbarOnly) itemSlot = 45;
        return itemSlot;
    }
}
