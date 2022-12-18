package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public class LMRRepositorySource implements RepositorySource {

    @Override
    public void loadPacks(Consumer<Pack> consumer) {

        PackHandler.prepareResourcesFolder();

        Pack p = PackHandler.createPack(this.createSupplier());
        if (p != null) {
            consumer.accept(p);
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Pack.ResourcesSupplier createSupplier() {
        return (s) -> {
            return new LMRPackResources();
        };
    }

}
