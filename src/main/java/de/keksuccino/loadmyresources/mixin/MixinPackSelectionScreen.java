package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.ReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;

@Mixin(PackSelectionScreen.class)
public class MixinPackSelectionScreen {

    //Hide the pack from the resource pack menu
    @Inject(at = @At("TAIL"), method = "updateList")
    private void onUpdatePackList(TransferableSelectionList widget, Stream<PackSelectionModel.Entry> packs, CallbackInfo info) {

        List<TransferableSelectionList.PackEntry> remove = new ArrayList<>();

        for (TransferableSelectionList.PackEntry e : widget.children()) {
            PackSelectionModel.Entry p = this.getPackFromEntry(e);
            if (p != null) {
                String name = p.getTitle().getString();
                if (name.equals(PackHandler.PACK_NAME)) {
                    remove.add(e);
                }
            }
        }

        for (TransferableSelectionList.PackEntry e : remove) {
            widget.children().remove(e);
        }

    }

    private PackSelectionModel.Entry getPackFromEntry(TransferableSelectionList.PackEntry entry) {
        try {
            Field f = ReflectionHelper.findField(TransferableSelectionList.PackEntry.class, "pack", "field_19129");
            f.setAccessible(true);
            return (PackSelectionModel.Entry) f.get(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
