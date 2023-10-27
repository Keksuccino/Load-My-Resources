package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.pack.PackHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
            PackSelectionModel.Entry p = this.getPackFromEntryLoadMyResources(e);
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

    @Unique
    private PackSelectionModel.Entry getPackFromEntryLoadMyResources(@NotNull TransferableSelectionList.PackEntry entry) {
        return ((IMixinTransferableSelectionListPackEntry)entry).getPackLoadMyResources();
    }

}
