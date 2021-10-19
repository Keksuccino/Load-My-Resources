package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.function.Supplier;

public class PackHandler {

    public static final String PACK_NAME = "loadmyresources.hiddenpack";
    public static final int PACK_FORMAT = 6;
    public static final String PACK_DESCRIPTION = "LMR Resources";
    public static final ResourceLocation DUMMY_PACK_META = new ResourceLocation("loadmyresources", "dummy.pack.mcmeta");

    public static File resourcesDirectory = new File("resources/");

    public static void init() {

        if (LoadMyResources.config != null) {
            resourcesDirectory = new File(LoadMyResources.config.getOrDefault("resource_path", "resources/"));
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Config not loaded! Can't get resource path! Path set to default 'resources/'!");
        }

        LoadMyResources.LOGGER.info("[LOAD MY RESOURCES] PackHandler initialized!");

    }

    public static void prepareResourcesFolder() {

        if (!resourcesDirectory.isDirectory()) {
            resourcesDirectory.mkdirs();
        }

    }

    public static ResourcePackInfo createPack(String name, PackMetadataSection meta, boolean forceEnablePack, Supplier<IResourcePack> packSupplier, ResourcePackInfo.IFactory constructor, ResourcePackInfo.Priority position, IPackNameDecorator source) {
        try {

            IResourcePack res = packSupplier.get();
            ResourcePackInfo pack = null;

            try {
                pack = constructor.create(name, forceEnablePack, packSupplier, res, meta, position, source);
            } catch (Throwable throwable1) {
                if (res != null) {
                    try {
                        res.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                }
                throw throwable1;
            }

            return pack;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


