package com.jasonvosmn.parasiteshud.net;

import com.jasonvosmn.parasiteshud.hud.GuiBarHUD;
import com.jasonvosmn.parasiteshud.items.ItemBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncSRP implements IMessage {
    private int phase;
    private int points;
    private int pointsNext;
    private int cooldown;

    public PacketSyncSRP() {}

    public PacketSyncSRP(int phase, int points, int pointsNext, int cooldown) {
        this.phase = phase;
        this.points = points;
        this.pointsNext = pointsNext;
        this.cooldown = cooldown;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.phase = buf.readInt();
        this.points = buf.readInt();
        this.pointsNext = buf.readInt();
        this.cooldown = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.phase);
        buf.writeInt(this.points);
        buf.writeInt(this.pointsNext);
        buf.writeInt(this.cooldown);
    }

    public static class Handler implements IMessageHandler<PacketSyncSRP, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSyncSRP message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                GuiBarHUD.updateFromPacket(message.phase, message.points, message.pointsNext, message.cooldown);
            });
            return null;
        }
    }
}

