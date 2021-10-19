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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class LMRPackResources extends FolderPackResources {

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
        InputStream in = Minecraft.getInstance().getResourceManager().getResource(PackHandler.DUMMY_PACK_META).getInputStream();
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
    public Collection<ResourceLocation> getResources(PackType type, String namespace, String resourcePreNamePath, int depth, Predicate<String> predicate) {
        List<ResourceLocation> l = Lists.newArrayList();
        this.listResources(new File(new File(this.file, namespace), resourcePreNamePath), depth, namespace, l, resourcePreNamePath + "/", predicate);
        return l;
    }

    private void listResources(File dir, int depth, String namespace, List<ResourceLocation> resourceList, String resourcePreNamePath, Predicate<String> predicate) {
        File[] files = dir.listFiles();
        if (files != null) {
            for(File f : files) {
                if (f.isDirectory()) {
                    if (depth > 0) {
                        this.listResources(f, depth - 1, namespace, resourceList, resourcePreNamePath + f.getName() + "/", predicate);
                    }
                } else if (!f.getName().endsWith(".mcmeta") && predicate.test(f.getName())) {
                    try {
                        resourceList.add(new ResourceLocation(namespace, resourcePreNamePath + f.getName()));
                    } catch (ResourceLocationException resourcelocationexception) {
                        LoadMyResources.LOGGER.error(resourcelocationexception.getMessage());
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
