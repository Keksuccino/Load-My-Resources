package de.keksuccino.loadmyresources;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Test {

    private static final ResourceLocation res = new ResourceLocation("testfolder", "megumin.jpg");

    @SubscribeEvent
    public void onRenderMain(GuiScreenEvent.DrawScreenEvent.Post e) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, res);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiComponent.blit(e.getMatrixStack(), 20, 20, 1.0F, 1.0F, 100, 100, 100, 100);

    }

}
