package com.seosean.zombiesautosplits.hudposition;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudCoordinate {
    public double x, y;
    public int absoluteX, absoluteY;
    private int width, height;
    public boolean isDragging;
    private int dragOffsetX, dragOffsetY;
    private int screenWidth, screenHeight;
    private int contents;
    private FontRenderer fontRendererObj;
    private boolean resizable;
    private int resizeCorner;

    public HudCoordinate(int contents, double x, double y, int width, int height, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isDragging = false;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.contents = contents;
        this.fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        this.resizable = true;
        this.resizeCorner = 0;
    }

    public int getContents(){
        return contents;
    }

    public void draw(GuiScreen gui) {
        this.absoluteX = (int) (x * screenWidth);
        this.absoluteY = (int) (y * screenHeight);
        int color = isDragging ? 0x80FFFFFF : 0x00000000;
        if(contents == 0){
            Gui.drawRect(absoluteX, absoluteY, absoluteX + width, absoluteY + height, color);
            this.fontRendererObj.drawStringWithShadow("0:00:0", absoluteX, absoluteY, 0xFFFFFF);
        }
    }

    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.gui instanceof ConfigGui) {
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= absoluteX && mouseX <= absoluteX + width && mouseY >= absoluteY && mouseY <= absoluteY + height;
    }

    public void onMousePressed(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            isDragging = true;
            dragOffsetX = absoluteX - mouseX;
            dragOffsetY = absoluteY - mouseY;
        }
    }

    public void onMouseReleased() {
        isDragging = false;
        resizable = false;
        resizeCorner = 0;
    }

    public void onMouseDragged(int mouseX, int mouseY) {
        if (isDragging) {
            absoluteX = mouseX + dragOffsetX;
            absoluteY = mouseY + dragOffsetY;
            x = (double)absoluteX / (double)screenWidth;
            y = (double)absoluteY / (double)screenHeight;

            if (x < 0) {
                absoluteX = 0;
                x = 0;
            } else if (absoluteX + width > screenWidth) {
                absoluteX = screenWidth - width;
                x = (double)absoluteX / (double)screenWidth;
            }
            if (y < 0) {
                absoluteY = 0;
                y = 0;
            } else if (absoluteY + height > screenHeight) {
                absoluteY = screenHeight - height;
                y = (double)absoluteY / (double)screenHeight;
            }
        }
    }
}