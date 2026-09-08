package com.jasonvosmn.parasiteshud;

import com.jasonvosmn.parasiteshud.hud.GuiBarHUD;
import com.jasonvosmn.parasiteshud.proxy.CommonProxy;
import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.jasonvosmn.parasiteshud.net.PacketSyncSRP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = SrpHUD.MODID, name = "parasiteshud", version = SrpHUD.VERSION, dependencies = "required-after:srparasites@[1.9.21,)", guiFactory = "com.jasonvosmn.parasiteshud.util.GuiFactory")

public class SrpHUD {
    public static final String MODID = "parasiteshud";
    public static final String VERSION = "1.2.4";
    public static SimpleNetworkWrapper NETWORK;

    @SidedProxy(clientSide = "com.jasonvosmn.parasiteshud.proxy.ClientProxy", serverSide = "com.jasonvosmn.parasiteshud.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        ConfigLoader.initConfig(event);
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("parasiteshud");
        NETWORK.registerMessage(PacketSyncSRP.Handler.class, PacketSyncSRP.class, 0, Side.CLIENT);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new GuiBarHUD());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

    }

}
