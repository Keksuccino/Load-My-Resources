package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

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

//        if (!ASSETS_DIR.isDirectory()) {
//            ASSETS_DIR.mkdirs();
//        }

//        int packFormat = 6;
//        String packDescription = "Resources loaded by 'Load My Resources'. Can't be disabled.";
//        createPackMetaFile(packFormat, packDescription, updatePackMetaProperties);

//        movePackFilesToAssets();

    }

//    protected static void movePackFilesToAssets() {
//        try {
//            for (File f : RESOURCES_DIR.listFiles()) {
//                if (!f.getName().endsWith("pack.mcmeta") && !f.getName().endsWith("assets")) {
//                    File to = new File(ASSETS_DIR.getPath() + "/" + f.getName());
//                    Files.move(f, to);
//                    System.out.println("[LOAD MY RESOURCES] Moved resources from pack root to 'assets': " + f.getName());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    protected static void createPackMetaFile(int packFormat, String description, boolean updateProperties) {
//        File packMeta = new File(RESOURCES_DIR.getPath() + "/pack.mcmeta");
//        try {
//            if (!packMeta.isFile()) {
//                packMeta.createNewFile();
//                updateProperties = true;
//            }
//            if (updateProperties) {
//                FileUtils.writeTextToFile(packMeta, false,
//                        "{",
//                        "  \"pack\": {",
//                        "    \"pack_format\": " + packFormat + ",",
//                        "    \"description\": \"" + description + "\"",
//                        "  }",
//                        "}");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static Pack createPack(String name, boolean forceEnablePack, Supplier<PackResources> packSupplier, Pack.PackConstructor constructor, Pack.Position position, PackSource source) {
        try {

            PackResources res = packSupplier.get();
            Pack pack = null;

            try {
                //To not need to create a pack.mcmeta file
                PackMetadataSection meta = new PackMetadataSection(new TextComponent(name), 6);
                //Forge Only - Remove 'res.isHidden()' in Fabric
                pack = constructor.create(name, new TextComponent(res.getName()), forceEnablePack, packSupplier, meta, position, source, res.isHidden());
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


