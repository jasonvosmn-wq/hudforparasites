package com.jasonvosmn.parasiteshud.hud;

import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.jasonvosmn.parasiteshud.util.ConfigLoader.pointsForStage;

@SideOnly(Side.CLIENT)
public class ParasitesHUD {
    private byte cachedStage = 0;
    private int cachedPoints = 0;
    private int cachedPointsNextPhase = 0;
    private int tickCounter = 0;
    private int hudX = 10;
    private int hudY = 10;
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
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.hideGUI || mc.player.getHealth() <= 0) {
            return;
        }

        FontRenderer fr = mc.fontRenderer;

        byte stage = cachedStage;
        int pointsNextStage = cachedPointsNextPhase;
        int points = cachedPoints;
        int pointsToNextPhase = 0;
        if (cachedPoints >= pointsForStage[9] ) {
            pointsToNextPhase = 0;
        } else {
            pointsToNextPhase = cachedPointsNextPhase - cachedPoints;
        }


        String[] hudLines = {
                "§6=== Parasites HUD ===",
                "§fСтадия: §e" + stage,
                "§fPoints: §e" + points,
                "§fОчков до следующей стадии: §e" + pointsToNextPhase
        };

        for (int i = 0; i < hudLines.length; i++) {
            fr.drawStringWithShadow(
                    hudLines[i],
                    hudX,
                    hudY + (i * fr.FONT_HEIGHT),
                    0xFFFFFF
            );
        }

    }
}