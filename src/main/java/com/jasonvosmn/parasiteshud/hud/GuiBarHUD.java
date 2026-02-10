package com.jasonvosmn.parasiteshud.hud;

import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import com.jasonvosmn.parasiteshud.util.KeyBindings;
import com.jasonvosmn.parasiteshud.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod.EventBusSubscriber(Side.CLIENT)
public class GuiBarHUD extends Gui {

    private static final ResourceLocation BAR_TEXTURE = new ResourceLocation("parasiteshud", "textures/gui/bar.png");

    private static byte cachedStage = 0;
    private static int cachedPoints = 0;
    private static int cachedPointsNextPhase = 0;
    private static int tickCounter = 0;
    private static final int UPDATE_INTERVAL = 20;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        // Проверяем, была ли нажата именно наша клавиша
        if (KeyBindings.keyEditHud.isPressed()) {
            // Открываем GUI
            Minecraft.getMinecraft().displayGuiScreen(new GuiMoveHUD());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter >= UPDATE_INTERVAL) {
                tickCounter = 0;
                updateCachedData();
            }
        }
    }

    private static void updateCachedData() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer clientPlayer = mc.player;
        if (clientPlayer == null) return;

        if (mc.isSingleplayer()) {
            net.minecraft.server.MinecraftServer server = mc.getIntegratedServer();
            if (server != null) {
                net.minecraft.entity.player.EntityPlayerMP serverPlayer =
                        server.getPlayerList().getPlayerByUsername(clientPlayer.getName());

                if (serverPlayer != null) {
                    try {
                        cachedStage = ConfigLoader.getPhase(serverPlayer);
                        cachedPoints = ConfigLoader.getTotalPoints(serverPlayer);
                        cachedPointsNextPhase = ConfigLoader.getPointsNextPhase(cachedStage);
                    } catch (Exception e) {
                        // Используй logger или просто удали в релизе
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution sr = event.getResolution();

            int barWidth = 142;
            int barHeight = 28;

            float scale = (float) ModConfig.hudScale;
            int x = (int) (ModConfig.hudX / scale);
            int y = (int) (ModConfig.hudY / scale);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.scale(scale, scale, scale);

            mc.getTextureManager().bindTexture(BAR_TEXTURE);

            int pointsNextStage = cachedPointsNextPhase > 0 ? cachedPointsNextPhase : 1;
            int points = cachedPoints;
            if (points > pointsNextStage) points = pointsNextStage;
            int pixelFill = (int) ((double) points / pointsNextStage * barWidth);

            //Координаты шкалы (u=29, v=32)
            mc.ingameGUI.drawTexturedModalRect(x+29, y+6, 29, 32, pixelFill, barHeight);
            mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, barWidth, barHeight);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}