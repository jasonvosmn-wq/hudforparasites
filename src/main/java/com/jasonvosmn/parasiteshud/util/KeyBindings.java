package com.jasonvosmn.parasiteshud.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding keyEditHud = new KeyBinding("key.parasiteshud.edit", Keyboard.KEY_K, "key.categories.parasiteshud");

    public static void register() {
        ClientRegistry.registerKeyBinding(keyEditHud);
    }
}
