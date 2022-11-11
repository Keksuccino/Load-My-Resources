package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.function.Supplier;

public class PackHandler {

    public static final String PACK_NAME = "loadmyresources.hiddenpack";
    public static final int PACK_FORMAT = 8;
    public static final String PACK_DESCRIPTION = "LMR Resources";
    public static final Identifier DUMMY_PACK_META = new Identifier("loadmyresources", "dummy.pack.mcmeta");

    public static File resourcesDirectory = new File(MinecraftClient.getInstance().runDirectory, "resources/");

    public static void init() {

        if (LoadMyResources.config != null) {
            resourcesDirectory = new File(MinecraftClient.getInstance().runDirectory, LoadMyResources.config.getOrDefault("resource_path", "resources/"));
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

    public static ResourcePackProfile createPack(String name, PackResourceMetadata meta, boolean forceEnablePack, Supplier<ResourcePack> packSupplier, ResourcePackProfile.Factory constructor, ResourcePackProfile.InsertionPosition position, ResourcePackSource source) {
        try {

            ResourcePack res = packSupplier.get();
            ResourcePackProfile pack = null;

            try {
                pack = constructor.create(name, new LiteralText(res.getName()), forceEnablePack, packSupplier, meta, position, source);
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


