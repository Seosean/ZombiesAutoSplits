package com.seosean.zombiesautosplits.handler;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import com.seosean.zombiesautosplits.hudposition.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class ConfigChangedHandler {

    private final ZombiesAutoSplits zombiesAutoSplits;

    private boolean loaded = false;

    public ConfigChangedHandler(ZombiesAutoSplits zombiesAutoSplits) {
        this.zombiesAutoSplits = Objects.requireNonNull(zombiesAutoSplits, "zombiesAutoSplits");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ZombiesAutoSplits.MODID)) {
            zombiesAutoSplits.reloadConfig();
        }
    }

    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if(this.loaded) return;
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!event.entity.worldObj.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) return;
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        if(!this.loaded) {
            this.loaded = true;
            new DelayedTask(() -> {
                IChatComponent globalTips = new ChatComponentText( EnumChatFormatting.GRAY + "ZombiesAutoSplits:" + EnumChatFormatting.GRAY + " use /splitshud to edit HUD position. ");
                IChatComponent glitchedTips = new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[Click when feature glitched]");
                IChatComponent glitchedTipsHover = new ChatComponentText(EnumChatFormatting.YELLOW + "Click to reset config file. \nGlitch like you can't edit HUD position will be fixed though resetting.");
                ChatStyle glitchTipsStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, glitchedTipsHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/splitshud reset"));
                glitchedTips.setChatStyle(glitchTipsStyle);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(globalTips.appendSibling(glitchedTips));
            }, 40);
        }
    }
}
