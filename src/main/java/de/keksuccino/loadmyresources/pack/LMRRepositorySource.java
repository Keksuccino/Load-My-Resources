package de.keksuccino.loadmyresources.pack;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class LMRRepositorySource implements RepositorySource {

    @Override
    public void loadPacks(@NotNull Consumer<Pack> consumer) {

        PackHandler.prepareResourcesFolder();

        Pack p = PackHandler.createPack(this.createSupplier());
        consumer.accept(p);

    }

    protected Pack.ResourcesSupplier createSupplier() {
        return new Pack.ResourcesSupplier() {
            @Override
            public @NotNull PackResources openPrimary(@NotNull String string) {
                return new LMRPackResources();
            }
            @Override
            public @NotNull PackResources openFull(@NotNull String string, Pack.@NotNull Info info) {
                return new LMRPackResources();
            }
        };
    }

}
