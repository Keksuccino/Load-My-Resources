package de.keksuccino.loadmyresources;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Test {

    private static final ResourceLocation res = new ResourceLocation("testfolder", "megumin.jpg");

    @SubscribeEvent
    public void onRenderMain(GuiScreenEvent.DrawScreenEvent.Post e) {

        Minecraft.getInstance().getTextureManager().bind(res);
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        AbstractGui.blit(e.getMatrixStack(), 20, 20, 1.0F, 1.0F, 100, 100, 100, 100);

    }

}
