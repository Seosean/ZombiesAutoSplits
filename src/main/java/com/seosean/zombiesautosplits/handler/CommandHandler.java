package com.seosean.zombiesautosplits.handler;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import com.seosean.zombiesautosplits.hudposition.ConfigGui;
import com.seosean.zombiesautosplits.hudposition.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.FMLLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandHandler extends CommandBase {
    public CommandHandler() {
    }

    public String getCommandName() {
        return "splitshud";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "splitshud";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            new DelayedTask(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new ConfigGui());
            }, 2);
        }else if(args.length == 1 && args[0].equals("reset")){
            IChatComponent configfolder = new ChatComponentText(EnumChatFormatting.RED.toString() + "The config file has been reset, you can now reedit with /splitshud");
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(configfolder);

            File fileBak = new File(ZombiesAutoSplits.getInstance().getConfig().getConfigFile().getAbsolutePath() + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
            FMLLog.severe("An exception occurred while loading config file %s. This file will be renamed to %s " +
                    "and a new config file will be generated.", ZombiesAutoSplits.getInstance().getConfig().getConfigFile().getName(), fileBak.getName());
            ZombiesAutoSplits.getInstance().getConfig().getConfigFile().renameTo(fileBak);
            ZombiesAutoSplits.getInstance().reloadConfig();
            ZombiesAutoSplits.getInstance().getConfig().load();
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    // $FF: synthetic method
    CommandHandler(Object x1) {
        this();
    }
}