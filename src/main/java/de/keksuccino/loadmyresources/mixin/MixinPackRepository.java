package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.LoadMyResources;
import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PackRepository.class)
public class MixinPackRepository {

    @ModifyVariable(at = @At("HEAD"), method = "<init>")
    private static RepositorySource[] onInit(RepositorySource[] providers) {

        if (LoadMyResources.init()) {

            List<RepositorySource> l = new ArrayList<RepositorySource>();
            for (RepositorySource p : providers) {
                l.add(p);
            }
            l.add(new LMRRepositorySource());

            LoadMyResources.LOGGER.info("[LOAD MY RESOURCES] Pack registered!");

            return l.toArray(new RepositorySource[0]);

        }

        return providers;

    }

}
