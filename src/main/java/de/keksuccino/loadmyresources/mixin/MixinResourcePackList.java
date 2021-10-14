package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.LoadMyResources;
import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ResourcePackList.class)
public class MixinResourcePackList {

    @ModifyVariable(at = @At("HEAD"), method = "<init>")
    private static IPackFinder[] onInit(IPackFinder[] providers) {

        if (LoadMyResources.init()) {

            List<IPackFinder> l = new ArrayList<IPackFinder>();
            for (IPackFinder p : providers) {
                l.add(p);
            }
            l.add(new LMRRepositorySource());

            System.out.println("[LOAD MY RESOURCES] Pack registered!");

            return l.toArray(new IPackFinder[0]);

        }

        return providers;

    }

}
