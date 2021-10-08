package de.keksuccino.loadmyresources.pack;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRRepositorySource implements IPackFinder {

    @Override
    public void loadPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory constructor) {

        PackHandler.prepareResourcesFolder();

        ResourcePackInfo p = PackHandler.createPack("", true, this.createSupplier(), constructor, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILT_IN);
        if (p != null) {
            consumer.accept(p);
        } else {
            System.err.println("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<IResourcePack> createSupplier() {
        return () -> {
            return new LMRPackResources();
        };
    }

}
