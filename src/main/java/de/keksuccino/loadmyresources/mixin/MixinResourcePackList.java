package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.events.RegisterRepositorySourceEvent;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackList;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ResourcePackList.class)
public class MixinResourcePackList {

    @Shadow @Final @Mutable private Set<IPackFinder> sources;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void onInitPost(CallbackInfo info) {

        ModLoader.get().postEvent(new RegisterRepositorySourceEvent(sources::add));

    }

}
