package de.keksuccino.loadmyresources.pack;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.metadata.LanguageResourceMetadata;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class LMRDirectoryResourcePack extends DirectoryResourcePack {

    public LMRDirectoryResourcePack() {
        super(PackHandler.resourcesDirectory);
    }

    @Override
    public String getName() {
        return "loadmyresources.hiddenpack";
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> serializer) {
        if (serializer == LanguageResourceMetadata.READER) {
            return (T) new LanguageResourceMetadata(new ArrayList<LanguageDefinition>());
        } else {
            return (T) new PackResourceMetadata(new LiteralText(""), 6);
        }
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Set<String> getNamespaces(ResourceType type) {
        Set<String> s = Sets.newHashSet();
        File[] files = this.base.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for(File f : files) {
                String path = relativize(this.base, f);
                if (path.equals(path.toLowerCase(Locale.ROOT))) {
                    s.add(path.substring(0, path.length() - 1));
                } else {
                    this.warnNonLowerCaseNamespace(path);
                }
            }
        }
        return s;
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public InputStream open(ResourceType type, Identifier location) throws IOException {
        return this.openFile(getPathFromLocation(location));
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public boolean contains(ResourceType type, Identifier location) {
        return this.containsFile(getPathFromLocation(location));
    }

    private static String getPathFromLocation(Identifier location) {
        return String.format("%s/%s", location.getNamespace(), location.getPath());
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String resourcePreNamePath, int depth, Predicate<String> predicate) {
        List<Identifier> l = Lists.newArrayList();
        this.listResources(new File(new File(this.base, namespace), resourcePreNamePath), depth, namespace, l, resourcePreNamePath + "/", predicate);
        return l;
    }

    private void listResources(File dir, int depth, String namespace, List<Identifier> resourceList, String resourcePreNamePath, Predicate<String> predicate) {
        File[] files = dir.listFiles();
        if (files != null) {
            for(File f : files) {
                if (f.isDirectory()) {
                    if (depth > 0) {
                        this.listResources(f, depth - 1, namespace, resourceList, resourcePreNamePath + f.getName() + "/", predicate);
                    }
                } else if (!f.getName().endsWith(".mcmeta") && predicate.test(f.getName())) {
                    try {
                        resourceList.add(new Identifier(namespace, resourcePreNamePath + f.getName()));
                    } catch (InvalidIdentifierException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            }
        }
    }

    //Forge Only - Mixins needed in Fabric version: Override 'updatePackList' in 'PackScreen'
//    @Override
//    public boolean isHidden() {
//        return true;
//    }

}
