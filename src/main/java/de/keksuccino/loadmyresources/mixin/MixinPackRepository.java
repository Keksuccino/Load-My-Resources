package de.keksuccino.loadmyresources.mixin;

import de.keksuccino.loadmyresources.LoadMyResources;
import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.*;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

@Mixin(PackRepository.class)
public class MixinPackRepository {

    @ModifyVariable(at = @At("HEAD"), method = "<init>", argsOnly = true)
    private static RepositorySource[] onInit(RepositorySource[] providers) {

        if (LoadMyResources.init()) {

            List<RepositorySource> l = new ArrayList<>(Arrays.asList(providers));
            l.add(new LMRRepositorySource());

            LoadMyResources.LOGGER.info("[LOAD MY RESOURCES] Pack registered!");

            return l.toArray(new RepositorySource[0]);

        }

        return providers;

    }

}
