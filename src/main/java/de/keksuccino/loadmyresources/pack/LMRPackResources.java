package de.keksuccino.loadmyresources.pack;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class LMRPackResources extends FolderPack {

    public LMRPackResources() {
        super(PackHandler.resourcesDirectory);
    }

    @Override
    public String getName() {
        return "Load My Resources";
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T> T getMetadataSection(IMetadataSectionSerializer<T> serializer) {
        if (serializer == LanguageMetadataSection.SERIALIZER) {
            return (T) new LanguageMetadataSection(new ArrayList<Language>());
        } else {
            return (T) new PackMetadataSection(new StringTextComponent(""), 6);
        }
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Set<String> getNamespaces(ResourcePackType type) {
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
    public InputStream getResource(ResourcePackType type, ResourceLocation location) throws IOException {
        return this.getResource(getPathFromLocation(location));
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public boolean hasResource(ResourcePackType type, ResourceLocation location) {
        return this.hasResource(getPathFromLocation(location));
    }

    private static String getPathFromLocation(ResourceLocation location) {
        return String.format("%s/%s", location.getNamespace(), location.getPath());
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Collection<ResourceLocation> getResources(ResourcePackType type, String namespace, String resourcePreNamePath, int depth, Predicate<String> predicate) {
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
                        System.err.println(resourcelocationexception.getMessage());
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
