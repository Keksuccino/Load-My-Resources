package de.keksuccino.loadmyresources.pack;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRResourcePackProvider implements ResourcePackProvider {

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory constructor) {

        PackHandler.prepareResourcesFolder();

        ResourcePackProfile p = PackHandler.createPack("loadmyresources.hiddenpack", true, this.createSupplier(), constructor, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (p != null) {
            consumer.accept(p);
        } else {
            System.err.println("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<ResourcePack> createSupplier() {
        return () -> {
            return new LMRDirectoryResourcePack();
        };
    }

}
