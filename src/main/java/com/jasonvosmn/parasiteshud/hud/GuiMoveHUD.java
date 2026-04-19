package com.jasonvosmn.parasiteshud.hud;

import com.jasonvosmn.parasiteshud.util.ModConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiMoveHUD extends GuiScreen {
    private final int barWidth = 142;
    private final int barHeight = 28;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        float scale = (float) ModConfig.hudScale;
        int x = ModConfig.hudX;
        int y = ModConfig.hudY;

        int scaledW = (int) (barWidth * scale);
        int scaledH = (int) (barHeight * scale);
        drawRect(x - 2, y - 2, x + scaledW + 2, y + scaledH + 2, 0x55FFFFFF);

        this.drawCenteredString(fontRenderer, "Перетаскивание: ЛКМ | Масштаб: Колесико мыши", width / 2, 20, 0xFFFFFF);
        this.drawCenteredString(fontRenderer, "Текущий масштаб: " + String.format("%.2f", scale), width / 2, 35, 0xAAAAAA);
        this.drawCenteredString(fontRenderer, "Нажмите ESC для сохранения", width / 2, height - 30, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // Получаем движение колесика мыши
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (wheel > 0) {
                ModConfig.hudScale = Math.min(2.0, ModConfig.hudScale + 0.05);
            } else {
                ModConfig.hudScale = Math.max(0.1, ModConfig.hudScale - 0.05);
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            ModConfig.hudX = mouseX - (int)(barWidth * ModConfig.hudScale / 2);
            ModConfig.hudY = mouseY - (int)(barHeight * ModConfig.hudScale / 2);
        }
    }

    @Override
    public void onGuiClosed() {
        ConfigManager.sync("parasiteshud", Config.Type.INSTANCE);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}