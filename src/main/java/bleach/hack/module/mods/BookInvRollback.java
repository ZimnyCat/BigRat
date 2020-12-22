// ORIGINAL BOOKDUPE: https://github.com/Oli-idk/BookDupe

package bleach.hack.module.mods;

import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.Finder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

public class BookInvRollback extends Module {
    public BookInvRollback() { super("BookInvRollback", KEY_UNBOUND, Category.EXPLOITS, "Rollbacks your inventory using writable book"); }

    @Override
    public void onEnable() {
        super.onEnable();
        Integer bookSlot = Finder.find(Items.WRITABLE_BOOK, true);
        if (bookSlot == null) {
            BleachLogger.errorMessage("No writable books found in hotbar! Disabling BookInvRollback...");
            setToggled(false);
            return;
        }
        mc.player.inventory.selectedSlot = bookSlot;
        final ItemStack itemStack = new ItemStack(Items.WRITABLE_BOOK, 1);
        final ListTag pages = new ListTag();
        pages.addTag(0, StringTag.of("DUPE"));
        itemStack.putSubTag("pages", pages);
        itemStack.putSubTag("title", StringTag.of(mc.player.getDisplayName().getString() + "'s cool book"));
        mc.getNetworkHandler().sendPacket(new BookUpdateC2SPacket(itemStack, true, mc.player.inventory.selectedSlot));
        setToggled(false);
    }
}
