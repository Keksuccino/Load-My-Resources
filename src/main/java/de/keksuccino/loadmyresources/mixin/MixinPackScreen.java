package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.ReflectionHelper;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(PackScreen.class)
public class MixinPackScreen {

    //Hide the pack from the resource pack menu
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/screen/pack/PackScreen;updatePackList(Lnet/minecraft/client/gui/screen/pack/PackListWidget;Ljava/util/stream/Stream;)V")
    private void onUpdatePackList(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo info) {

        List<PackListWidget.ResourcePackEntry> remove = new ArrayList<>();

        for (PackListWidget.ResourcePackEntry e : widget.children()) {
            ResourcePackOrganizer.Pack p = this.getPackFromEntry(e);
            if (p != null) {
                String name = p.getDisplayName().getString();
                if (name.equals(PackHandler.PACK_NAME)) {
                    remove.add(e);
                }
            }
        }

        for (PackListWidget.ResourcePackEntry e : remove) {
            widget.children().remove(e);
        }

    }

    private ResourcePackOrganizer.Pack getPackFromEntry(PackListWidget.ResourcePackEntry entry) {
        try {
            Field f = ReflectionHelper.findField(PackListWidget.ResourcePackEntry.class, "pack", "field_19129");
            f.setAccessible(true);
            return (ResourcePackOrganizer.Pack) f.get(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
