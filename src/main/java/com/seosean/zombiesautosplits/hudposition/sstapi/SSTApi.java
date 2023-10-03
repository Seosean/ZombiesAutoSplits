package com.seosean.zombiesautosplits.hudposition.sstapi;

import com.seosean.showspawntime.api.ApiHandler;
import com.seosean.showspawntime.api.ConfigApi;
import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public class SSTApi {
    public static ConfigApi modApi;

    public static boolean isShowSpawnTimeInstalled(){
        List<ModContainer> mods = Loader.instance().getActiveModList();
        for (ModContainer mod : mods) {
            if ("ShowSpawnTime".equals(mod.getModId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean update(){
        if (ApiHandler.getInstance().getApiInstance(ConfigApi.class) != null) {
            modApi = ApiHandler.getInstance().getApiInstance(ConfigApi.class);
            ZombiesAutoSplits.XSplitter = modApi.getX();
            ZombiesAutoSplits.YSplitter = modApi.getY();
            ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "XSplitter", -1).set(modApi.getX());
            ZombiesAutoSplits.getInstance().getConfig().get(Configuration.CATEGORY_CLIENT, "YSplitter", -1).set(modApi.getY());
            ZombiesAutoSplits.getInstance().getConfig().save();
            return true;
        }
        return false;
    }
}
