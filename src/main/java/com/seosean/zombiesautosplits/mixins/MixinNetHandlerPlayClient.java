package com.seosean.zombiesautosplits.mixins;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import com.seosean.zombiesautosplits.splitter.LiveSplitSplitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.*;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    public MixinNetHandlerPlayClient() {
        minecraft = Minecraft.getMinecraft();
        logger = ZombiesAutoSplits.getInstance().getLogger();
    }

    @Inject(method = "handleSoundEffect", at = @At(value = "HEAD"))
    private void broadcastSound(S29PacketSoundEffect packetIn, CallbackInfo callbackInfo){
        this.detectSound(packetIn);
    }

    private final Minecraft minecraft;

    private final Logger logger;

    private LiveSplitSplitter splitter;

    private boolean enabled = true;

    private boolean AAr10 = false;


    private void detectSound(S29PacketSoundEffect packetIn){
        String soundEffect = packetIn.getSoundName();
        if (soundEffect.equals("mob.wither.spawn") || soundEffect.equals("mob.enderdragon.end") || (soundEffect.equals("mob.guardian.curse") && !AAr10)) {
            if(!enabled){
                return;
            }
            splitter = ZombiesAutoSplits.getInstance().getSplitter();
            AAr10 = soundEffect.equals("mob.guardian.curse");
            splitter.startOrSplit().exceptionally(throwable -> {
                logger.warn("Failed to split", throwable);
                minecraft.addScheduledTask(() -> {
                    if (minecraft.thePlayer != null) {
                        IChatComponent message = new ChatComponentText("Failed to split!");
                        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                        minecraft.thePlayer.addChatComponentMessage(message);
                    }
                });

                return null;
            });
        }
    }

}
