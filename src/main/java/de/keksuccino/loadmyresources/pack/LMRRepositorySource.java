package de.keksuccino.loadmyresources.pack;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRRepositorySource implements RepositorySource {

    @Override
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor constructor) {

        PackHandler.prepareResourcesFolder();

        Pack p = PackHandler.createPack("", true, this.createSupplier(), constructor, Pack.Position.TOP, PackSource.BUILT_IN);
        if (p != null) {
            consumer.accept(p);
        } else {
            System.err.println("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<PackResources> createSupplier() {
        return () -> {
            return new LMRPackResources();
        };
    }

}
