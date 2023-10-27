//package de.keksuccino.loadmyresources.mixin;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.screens.TitleScreen;
//import net.minecraft.resources.ResourceLocation;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(TitleScreen.class)
//public class MixinTitleScreen {
//
//    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("lmr", "megumin.jpg");
//    private static final ResourceLocation RESOURCE_LOCATION_2 = new ResourceLocation("lmr", "testfolder/megumin.jpg");
//
//    @Inject(method = "render", at = @At("RETURN"))
//    private void onRenderPostLMR(GuiGraphics graphics, int i, int j, float f, CallbackInfo info) {
//
//        RenderSystem.enableBlend();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        graphics.blit(RESOURCE_LOCATION, 20, 20, 0.0F, 0.0F, 100, 100, 100, 100);
//        RenderSystem.disableBlend();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//        RenderSystem.enableBlend();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        graphics.blit(RESOURCE_LOCATION_2, 20, 130, 0.0F, 0.0F, 100, 100, 100, 100);
//        RenderSystem.disableBlend();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }
//
//}
