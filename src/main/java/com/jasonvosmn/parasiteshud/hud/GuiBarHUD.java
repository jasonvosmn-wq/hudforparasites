package com.jasonvosmn.parasiteshud.hud;

import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.jasonvosmn.parasiteshud.util.ConfigLoader.pointsForStage;

@Mod.EventBusSubscriber
public class GuiBarHUD extends Gui {

    // Путь к твоей текстуре шкалы
    private static final ResourceLocation BAR_TEXTURE = new ResourceLocation("parasiteshud", "textures/gui/bar.png");

    private static byte cachedStage = 0;
    private static int cachedPoints = 0;
    private static int cachedPointsNextPhase = 0;
    private static int tickCounter = 0;
    private static final int UPDATE_INTERVAL = 20;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Обновляем кэш только на тиках клиента (не на тиках игрока)
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;

            // Обновляем данные с заданным интервалом
            if (tickCounter >= UPDATE_INTERVAL) {
                tickCounter = 0;
                updateCachedData();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateCachedData() {
        Minecraft mc = Minecraft.getMinecraft();

        // 1. Получаем клиентского игрока
        EntityPlayer clientPlayer = mc.player;
        if (clientPlayer == null) return;

        // 2. Для однопользовательской игры
        if (mc.isSingleplayer()) {
            // Получаем сервер
            net.minecraft.server.MinecraftServer server = mc.getIntegratedServer();
            if (server != null) {

                // Ищем серверного игрока
                net.minecraft.entity.player.EntityPlayerMP serverPlayer =
                        server.getPlayerList().getPlayerByUsername(clientPlayer.getName());

                if (serverPlayer != null) {
                    try {
                        // Теперь получаем данные с СЕРВЕРА
                        cachedStage = ConfigLoader.getPhase(serverPlayer);
                        cachedPoints = ConfigLoader.getTotalPoints(serverPlayer);
                        cachedPointsNextPhase = ConfigLoader.getPointsNextPhase(cachedStage);
                    } catch (Exception e) {
                        System.out.println("Ошибка получения данных: " + e.getMessage());
                    }
                } else {
                    System.out.println("Серверный игрок не найден!");
                }
            } else {
                System.out.println("Сервер не найден!");
            }
        } else {
            System.out.println("Мультиплеер - нужны пакеты с сервера");
            // Для мультиплеера пока оставляем 0
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution sr = event.getResolution();

            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();

            int barWidth = 91;
            int barHeight = 11;
            int x = (screenWidth / 2) - (barWidth / 2);
            int y = screenHeight - 60;

            mc.getTextureManager().bindTexture(BAR_TEXTURE);

            mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, barWidth, barHeight);

            byte stage = cachedStage;
            int pointsNextStage = cachedPointsNextPhase;
            int points = cachedPoints;
            int pointsToNextPhase;
            if (cachedPoints >= pointsForStage[9]) {
                pointsToNextPhase = 0;
            } else {
                pointsToNextPhase = cachedPointsNextPhase - cachedPoints;
            }


            int pixelFill = (int) ((double) points / pointsNextStage * barWidth);

            mc.ingameGUI.drawTexturedModalRect(x, y, 0, 15, pixelFill, barHeight);

            FontRenderer fr = mc.fontRenderer;

            String[] hudLines = {
                    "§fStage: §e" + stage,
                    "§fPoints: §e" + points,
                    "§fPoints to the next stage: §e" + pointsToNextPhase
            };

            for (int i = 0; i < hudLines.length; i++) {
                fr.drawStringWithShadow(
                        hudLines[i],
                        10,
                        5 + (i * fr.FONT_HEIGHT),
                        0xFFFFFF
                );
            }

        }
    }
}