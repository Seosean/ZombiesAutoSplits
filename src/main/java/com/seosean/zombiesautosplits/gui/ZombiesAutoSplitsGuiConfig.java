package com.seosean.zombiesautosplits.gui;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ZombiesAutoSplitsGuiConfig extends GuiConfig {

    public ZombiesAutoSplitsGuiConfig(GuiScreen parent) {
        super(parent,
                new ConfigElement(ZombiesAutoSplits.getInstance().getConfig()
                        .getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                ZombiesAutoSplits.MODID, false, false,
                "ZombiesAutoSplits Configuration");
    }

}
