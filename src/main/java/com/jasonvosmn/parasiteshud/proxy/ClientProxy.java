package com.jasonvosmn.parasiteshud.proxy;

import com.jasonvosmn.parasiteshud.hud.GuiBarHUD;
import com.jasonvosmn.parasiteshud.util.KeyBindings;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    private GuiBarHUD guiBarHUD;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (event.getSide().isClient()) {
            KeyBindings.register();
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}