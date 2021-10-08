package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.LoadMyResources;
import de.keksuccino.loadmyresources.pack.LMRResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@Mixin(ResourcePackManager.class)
public class MixinResourcePackManager {

    @ModifyVariable(at = @At("HEAD"), method = "<init>")
    private static ResourcePackProvider[] onInit(ResourcePackProvider[] providers) {

        if (LoadMyResources.init()) {

            List<ResourcePackProvider> l = new ArrayList<>();
            for (ResourcePackProvider p : providers) {
                l.add(p);
            }
            l.add(new LMRResourcePackProvider());

            System.out.println("[LOAD MY RESOURCES] Pack registered!");

            return l.toArray(new ResourcePackProvider[0]);

        }

        return providers;

    }

}
