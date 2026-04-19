package com.jasonvosmn.parasiteshud.util;

import com.jasonvosmn.parasiteshud.SrpHUD; // Замени на имя своего главного класса!
import com.jasonvosmn.parasiteshud.net.PacketSyncSRP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class ServerEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Выполняем только на СЕРВЕРЕ и только в конце тика игрока
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            if (player.ticksExisted % 20 == 0) {
                int phase = ConfigLoader.getPhase(player);
                int points = ConfigLoader.getTotalPoints(player);
                int next = ConfigLoader.getPointsNextPhase((byte)phase);
                int cooldown = ConfigLoader.getCooldown(player);
                // Отправляем пакет именно этому игроку
                SrpHUD.NETWORK.sendTo(new PacketSyncSRP(phase, points, next, cooldown), player);
            }
        }
    }
}