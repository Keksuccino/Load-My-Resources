package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;

import java.io.File;
import java.util.function.Supplier;

public class PackHandler {

    public static File resourcesDirectory = new File("resources/");

    public static void init() {

        if (LoadMyResources.config != null) {
            resourcesDirectory = new File(LoadMyResources.config.getOrDefault("resource_path", "resources/"));
        } else {
            System.err.println("[LOAD MY RESOURCES] ERROR: Config not loaded! Can't get resource path! Path set to default 'resources/'!");
        }

        System.out.println("[LOAD MY RESOURCES] PackHandler initialized!");

    }

    public static void prepareResourcesFolder() {

        if (!resourcesDirectory.isDirectory()) {
            resourcesDirectory.mkdirs();
        }

    }

    public static ResourcePackProfile createPack(String name, boolean forceEnablePack, Supplier<ResourcePack> packSupplier, ResourcePackProfile.Factory constructor, ResourcePackProfile.InsertionPosition position, ResourcePackSource source) {
        try {

            ResourcePack res = packSupplier.get();
            ResourcePackProfile pack = null;

            try {
                //To not need to create a pack.mcmeta file
                PackResourceMetadata meta = new PackResourceMetadata(new LiteralText(name), 6);
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


