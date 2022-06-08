package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRRepositorySource implements RepositorySource {

    @Override
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor constructor) {

        PackHandler.prepareResourcesFolder();

        PackMetadataSection meta = new PackMetadataSection(Component.literal(PackHandler.PACK_NAME), PackHandler.PACK_FORMAT);
        Pack p = PackHandler.createPack(PackHandler.PACK_NAME, meta, true, this.createSupplier(), constructor, Pack.Position.TOP, PackSource.DEFAULT);
        if (p != null) {
            consumer.accept(p);
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<PackResources> createSupplier() {
        return () -> {
            return new LMRPackResources();
        };
    }

}
