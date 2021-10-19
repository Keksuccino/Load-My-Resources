package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRResourcePackProvider implements ResourcePackProvider {

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory constructor) {

        PackHandler.prepareResourcesFolder();

        PackResourceMetadata meta = new PackResourceMetadata(new LiteralText(PackHandler.PACK_NAME), PackHandler.PACK_FORMAT);
        ResourcePackProfile p = PackHandler.createPack(PackHandler.PACK_NAME, meta, true, this.createSupplier(), constructor, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
        if (p != null) {
            consumer.accept(p);
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<ResourcePack> createSupplier() {
        return () -> {
            return new LMRDirectoryResourcePack();
        };
    }

}
