package com.seosean.zombiesautosplits.hudposition;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiScreen {
   private int selected = -1;
   private int scroll = 0;
   private boolean isDragging;
   private Point dragOffset;
   private List<HudCoordinate> boxes = new ArrayList<>();

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();

      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

      int button = Mouse.getEventButton();
      boolean mouseButtonDown = Mouse.getEventButtonState();

      for (HudCoordinate box : boxes) {
         if (box.isMouseOver(mouseX, mouseY)) {
            if (button == 0) { // 左键
               if (mouseButtonDown) { // 鼠标按下
                  box.onMousePressed(mouseX, mouseY);
               } else { // 鼠标释放
                  box.onMouseReleased();
               }
            }
         }

         if (box.isDragging) { // 鼠标拖动
            box.onMouseDragged(mouseX, mouseY);
         }
      }
   }

   public void initGui() {
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Pre(this, buttonList));
      super.initGui();
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this, buttonList));
      int widthTime = this.fontRendererObj.getStringWidth("0:00:0");
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 82, sr.getScaledHeight() - 25, 80, 20, "Done"));
      this.buttonList.add(new GuiButton(2, sr.getScaledWidth() / 2 - 82 + 85, sr.getScaledHeight() - 25, 80, 20, "Reset"));
      boxes.clear();
      HudCoordinate boxSplitter = new HudCoordinate(0, ZombiesAutoSplits.getInstance().getXSplitter(), ZombiesAutoSplits.getInstance().getYSplitter(), widthTime, this.fontRendererObj.FONT_HEIGHT, this.width, this.height);
      boxes.add(boxSplitter);
   }

   protected void mouseClicked(int mouseX, int mouseY, int p_mouseClicked_3_) throws IOException {
      super.mouseClicked(mouseX, mouseY, p_mouseClicked_3_);
   }

   protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
      super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
   }

   protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
      super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
   }

   public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawDefaultBackground();
      for (HudCoordinate box : boxes) {
         box.draw(this);
      }
      this.fontRendererObj.drawStringWithShadow("ZombiesAutoSplits", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("ZombiesAutoSplits") / 2.0F, 10.0F, Color.WHITE.getRGB());
      this.fontRendererObj.drawStringWithShadow("Click \"Done\" to save your current HUD position settings.", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("Click \"Done\" to save your current HUD position settings.") / 2.0F, this.height/2, Color.WHITE.getRGB());

      super.drawScreen(mouseX, mouseY, p_drawScreen_3_);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
         case 1: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSplitter", -1).set(box.x);
                  ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSplitter", -1).set(box.y);
                  ZombiesAutoSplits.XSplitter = box.x;
                  ZombiesAutoSplits.YSplitter = box.y;
               }
            }
            this.mc.displayGuiScreen(null);
            break;
         }
         case 2: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSplitter", -1).set(-1);
                  ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSplitter", -1).set(-1);
                  ZombiesAutoSplits.XSplitter = -1;
                  ZombiesAutoSplits.YSplitter = -1;
                  new DelayedTask(() -> {
                     box.x = ZombiesAutoSplits.getInstance().getXSplitter();
                     box.absoluteX = (int)(ZombiesAutoSplits.getInstance().getXSplitter() * this.width);
                     box.y = ZombiesAutoSplits.getInstance().getYSplitter();
                     box.absoluteY = (int)(ZombiesAutoSplits.getInstance().getYSplitter() * this.height);
                  }, 2);
               }
            }
            break;
         }
      }
      ZombiesAutoSplits.getInstance().getConfig().save();
   }

   public void onGuiClosed() {
      for (HudCoordinate box : boxes) {
         if (box.getContents() == 0) {
            ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSplitter", -1).set(box.x);
            ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSplitter", -1).set(box.y);
            ZombiesAutoSplits.XSplitter = box.x;
            ZombiesAutoSplits.YSplitter = box.y;
         }
      }
      ZombiesAutoSplits.getInstance().getConfig().save();
      super.onGuiClosed();
   }
}
