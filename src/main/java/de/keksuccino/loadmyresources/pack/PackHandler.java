package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.text.StringTextComponent;

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

//    public static ResourcePackInfo create(String name, boolean forceEnablePack, Supplier<IResourcePack> packSupplier, ResourcePackInfo.IFactory constructor, ResourcePackInfo.Priority position, IPackNameDecorator source) {
//        try (IResourcePack iresourcepack = packSupplier.get()) {
//            PackMetadataSection packmetadatasection = iresourcepack.getMetadataSection(PackMetadataSection.SERIALIZER);
//            if (forceEnablePack && packmetadatasection == null) {
//                packmetadatasection = BROKEN_ASSETS_FALLBACK;
//            }
//
//            if (packmetadatasection != null) {
//                return constructor.create(name, forceEnablePack, packSupplier, iresourcepack, packmetadatasection, position, source);
//            }
//
//            LOGGER.warn("Couldn't find pack meta for pack {}", (Object)name);
//        } catch (IOException ioexception) {
//            LOGGER.warn("Couldn't get pack info for: {}", (Object)ioexception.toString());
//        }
//
//        return null;
//    }

    public static ResourcePackInfo createPack(String name, boolean forceEnablePack, Supplier<IResourcePack> packSupplier, ResourcePackInfo.IFactory constructor, ResourcePackInfo.Priority position, IPackNameDecorator source) {
        try {

            IResourcePack res = packSupplier.get();
            ResourcePackInfo pack = null;

            try {
                //To not need to create a pack.mcmeta file
                PackMetadataSection meta = new PackMetadataSection(new StringTextComponent(""), 1);
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


