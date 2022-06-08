package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import java.io.File;
import java.util.function.Supplier;

public class PackHandler {

    public static final String PACK_NAME = "loadmyresources.hiddenpack";
    public static final int PACK_FORMAT = 8;
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

    public static Pack createPack(String name, PackMetadataSection meta, boolean forceEnablePack, Supplier<PackResources> packSupplier, Pack.PackConstructor constructor, Pack.Position position, PackSource source) {
        try {

            PackResources res = packSupplier.get();
            Pack pack = null;

            try {
                pack = constructor.create(name, Component.literal(res.getName()), forceEnablePack, packSupplier, meta, position, source);
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

            if (res != null) {
                res.close();
            }

            return pack;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


