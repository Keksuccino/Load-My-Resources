package de.keksuccino.loadmyresources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Test {

    private static final ResourceLocation res = new ResourceLocation("testfolder", "megumin.jpg");

    @SubscribeEvent
    public void onRenderMain(GuiScreenEvent.DrawScreenEvent.Post e) {

        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(20, 20, 1.0F, 1.0F, 100, 100, 100, 100);

    }

}
