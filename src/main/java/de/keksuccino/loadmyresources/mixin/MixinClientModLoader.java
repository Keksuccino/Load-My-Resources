package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.events.RegisterRepositorySourceEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DownloadingPackFinder;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.client.ClientModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientModLoader.class)
public class MixinClientModLoader {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/packs/ResourcePackLoader;loadResourcePacks(Lnet/minecraft/resources/ResourcePackList;Ljava/util/function/BiFunction;)V", shift = At.Shift.AFTER), method = "begin", remap = false)
    private static void onBegin(Minecraft minecraft, ResourcePackList defaultResourcePacks, IReloadableResourceManager mcResourceManager, DownloadingPackFinder metadataSerializer, CallbackInfo info) {

        ModLoader.get().postEvent(new RegisterRepositorySourceEvent(defaultResourcePacks::addPackFinder));

    }

}
