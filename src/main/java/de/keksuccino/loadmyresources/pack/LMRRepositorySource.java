package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.resources.*;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.text.StringTextComponent;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LMRRepositorySource extends FolderPackFinder {

    public LMRRepositorySource() {
        super(new File("dummy"), IPackNameDecorator.DEFAULT);
    }

    @Override
    public void loadPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory constructor) {

        PackHandler.prepareResourcesFolder();

        PackMetadataSection meta = new PackMetadataSection(new StringTextComponent(PackHandler.PACK_DESCRIPTION), PackHandler.PACK_FORMAT);
        ResourcePackInfo p = PackHandler.createPack(PackHandler.PACK_NAME, meta, true, this.createSupplier(), constructor, ResourcePackInfo.Priority.TOP, IPackNameDecorator.DEFAULT);
        if (p != null) {
            consumer.accept(p);
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Failed to create pack: " + PackHandler.resourcesDirectory.getAbsolutePath());
        }

    }

    protected Supplier<IResourcePack> createSupplier() {
        return () -> {
            return new LMRPackResources();
        };
    }

}
