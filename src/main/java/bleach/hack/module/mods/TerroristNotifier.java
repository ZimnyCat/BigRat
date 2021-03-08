package bleach.hack.module.mods;

import bleach.hack.event.events.EventEntityRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class TerroristNotifier extends Module {

    List<String> terrorists = new ArrayList<>();

    public TerroristNotifier() {
        super("TerroristNotifier", KEY_UNBOUND, Category.MISC, "Notifies when a terrorist enters your render distance");
    }
    @Subscribe
    public void terroristFinder (EventEntityRender event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity terrorist = (PlayerEntity) event.getEntity();
        if (terrorist != mc.player && checkHands(terrorist)
                && !terrorists.contains(terrorist.getName().asString())) {
            terrorists.add(terrorist.getName().asString());
            BleachLogger.infoMessage("Terrorist found [\u00a73" + terrorist.getName().asString() + "\u00a7f]");
        }
    }
    private boolean checkHands(PlayerEntity nigga) {
        BedBomb ab = new BedBomb();
        Item mainHandItem = nigga.getMainHandStack().getItem();
        Item offHandItem = nigga.getOffHandStack().getItem();
        return (mainHandItem == Items.END_CRYSTAL || (mainHandItem instanceof BedItem && ab.dimensionCheck()))
                    || (offHandItem == Items.END_CRYSTAL || (offHandItem instanceof BedItem && ab.dimensionCheck()));
    }
}
