package de.keksuccino.loadmyresources.pack;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class LMRPackResources extends FolderPackResources {

    private static final Logger LOGGER = LogManager.getLogger("loadmyresources/LMRPackResources");

    public LMRPackResources() {
        super(PackHandler.resourcesDirectory);
    }

    @Override
    public String getName() {
        return PackHandler.PACK_NAME;
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) throws IOException {
        InputStream in = Minecraft.getInstance().getResourceManager().open(PackHandler.DUMMY_PACK_META);
        Object o;
        try {
            o = getMetadataFromStream(serializer, in);
        } catch (Throwable throwable1) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable throwable) {
                    throwable1.addSuppressed(throwable);
                }
            }
            throw throwable1;
        }

        if (in != null) {
            in.close();
        }

        return (T)o;
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Set<String> getNamespaces(PackType type) {
        Set<String> s = Sets.newHashSet();
        File[] files = this.file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for(File f : files) {
                String path = getRelativePath(this.file, f);
                if (path.equals(path.toLowerCase(Locale.ROOT))) {
                    s.add(path.substring(0, path.length() - 1));
                } else {
                    this.logWarning(path);
                }
            }
        }
        return s;
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public InputStream getResource(PackType type, ResourceLocation location) throws IOException {
        return this.getResource(getPathFromLocation(location));
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public boolean hasResource(PackType type, ResourceLocation location) {
        return this.hasResource(getPathFromLocation(location));
    }

    private static String getPathFromLocation(ResourceLocation location) {
        return String.format("%s/%s", location.getNamespace(), location.getPath());
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Collection<ResourceLocation> getResources(PackType packType, String namespace, String resourcePreNamePath, Predicate<ResourceLocation> predicate) {
        List<ResourceLocation> l = Lists.newArrayList();
        this.listResources(new File(new File(this.file, namespace), resourcePreNamePath), namespace, l, resourcePreNamePath + "/", predicate);
        return l;
    }

    private void listResources(File file, String string, List<ResourceLocation> list, String string2, Predicate<ResourceLocation> predicate) {
        File[] files = file.listFiles();
        if (files != null) {
            File[] var7 = files;
            int var8 = files.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                File file2 = var7[var9];
                if (file2.isDirectory()) {
                    this.listResources(file2, string, list, string2 + file2.getName() + "/", predicate);
                } else if (!file2.getName().endsWith(".mcmeta")) {
                    try {
                        String string3 = string2 + file2.getName();
                        ResourceLocation resourceLocation = ResourceLocation.tryBuild(string, string3);
                        if (resourceLocation == null) {
                            LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", string, string3);
                        } else if (predicate.test(resourceLocation)) {
                            list.add(resourceLocation);
                        }
                    } catch (ResourceLocationException var13) {
                        LOGGER.error(var13.getMessage());
                    }
                }
            }
        }
    }

    //Forge Only - Mixins needed in Fabric version: Override 'updatePackList' in 'PackScreen'
    @Override
    public boolean isHidden() {
        return true;
    }

}
