package de.keksuccino.loadmyresources.mixin;

import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransferableSelectionList.PackEntry.class)
public interface IMixinTransferableSelectionListPackEntry {

    @Accessor("pack") PackSelectionModel.Entry getPackLoadMyResources();

}
