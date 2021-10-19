package de.keksuccino.loadmyresources.events;

import net.minecraft.resources.IPackFinder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.function.Consumer;

public class RegisterRepositorySourceEvent extends Event implements IModBusEvent {

    private Consumer<IPackFinder> sources;

    public RegisterRepositorySourceEvent(Consumer<IPackFinder> sources) {
        this.sources = sources;
    }

    public void addRepositorySource(IPackFinder source) {
        this.sources.accept(source);
    }

}
