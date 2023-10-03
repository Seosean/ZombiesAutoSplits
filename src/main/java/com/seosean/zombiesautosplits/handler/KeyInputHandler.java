package com.seosean.zombiesautosplits.handler;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class KeyInputHandler {

    private final Minecraft minecraft;

    private final Logger logger;

    private final KeyBinding keyBinding;

    public KeyInputHandler(Minecraft minecraft, Logger logger, KeyBinding keyBinding) {
        this.minecraft = Objects.requireNonNull(minecraft, "minecraft");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.keyBinding = Objects.requireNonNull(keyBinding, "keyBinding");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) {
            IChatComponent toggledComponent;
            boolean toggled = ZombiesAutoSplits.getInstance().toggle();
            if (toggled) {
                toggledComponent = new ChatComponentText("ON")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
            }
            else {
                toggledComponent = new ChatComponentText("OFF")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
            }

            IChatComponent message = new ChatComponentText("")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))
                    .appendSibling(new ChatComponentText("Toggled AutoSplits "))
                    .appendSibling(toggledComponent)
                    .appendSibling(new ChatComponentText("!"));
            minecraft.thePlayer.addChatMessage(message);
        }
    }


}
