package com.seosean.zombiesautosplits;

import com.google.common.base.Supplier;
import com.seosean.zombiesautosplits.handler.CommandHandler;
import com.seosean.zombiesautosplits.handler.ConfigChangedHandler;
import com.seosean.zombiesautosplits.handler.KeyInputHandler;
import com.seosean.zombiesautosplits.handler.RenderTimeHandler;
import com.seosean.zombiesautosplits.splitter.LiveSplitSplitter;
import com.seosean.zombiesautosplits.splitter.internal.InternalSplitter;
import com.seosean.zombiesautosplits.splitter.socket.LiveSplitSocketSplitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod(
   modid = ZombiesAutoSplits.MODID,
        name = "Zombies AutoSplits",
        version = "1.2",
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true,
        guiFactory = "com.seosean.zombiesautosplits.gui.ZombiesAutoSplitsGuiFactory")

public class ZombiesAutoSplits {

   public static final String MODID = "zombiesautosplits";

   public static ZombiesAutoSplits instance;

   private boolean isShowSpawnTimeInstalled;

   private final KeyBinding autoSplitsKeybind = new KeyBinding("Toggle AutoSplits", Keyboard.KEY_NONE,
           "Tahmid's Mods");

   private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

   private Logger logger;

   private Configuration config;

   private InternalSplitter internalSplitter;

   private RenderTimeHandler renderTimeHandler;

   private LiveSplitSplitter splitter;

   public boolean isShowSpawnTimeInstalled() {
      return isShowSpawnTimeInstalled;
   }

   public LiveSplitSplitter getSplitter() {
      return splitter;
   }

   public static ZombiesAutoSplits getInstance() {
      return instance;
   }

   public Logger getLogger() {
      return this.logger;
   }

   public Minecraft minecraft;

   public FontRenderer fontRenderer;

   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      logger = event.getModLog();

      config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();

      splitter = createSplitter();
      if (splitter instanceof InternalSplitter) {
         internalSplitter = (InternalSplitter) splitter;
      }
   }
   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(new ConfigChangedHandler(this));
      MinecraftForge.EVENT_BUS.register(new KeyInputHandler(Minecraft.getMinecraft(), logger, autoSplitsKeybind));
      minecraft = Minecraft.getMinecraft();
      int color = 0xFFFFFF;
      renderTimeHandler = new RenderTimeHandler(minecraft, minecraft.fontRendererObj, color);
      if (internalSplitter != null) {
         renderTimeHandler.setSplitter(internalSplitter);
      }
      MinecraftForge.EVENT_BUS.register(renderTimeHandler);


      ClientRegistry.registerKeyBinding(autoSplitsKeybind);

      ClientCommandHandler.instance.registerCommand(new CommandHandler());

      instance = this;

      fontRenderer = minecraft.fontRendererObj;

      isShowSpawnTimeInstalled = ((Supplier<Boolean>) () -> {
         List<ModContainer> mods = Loader.instance().getActiveModList();
         for (ModContainer mod : mods) {
            if ("showspawntime".equals(mod.getModId())) {
               return true;
            }
         }
         return false;
      }).get();
   }

   public Configuration getConfig() {
      return config;
   }

   public void reloadConfig() {
      if (internalSplitter != null) {
         internalSplitter.cancel();
         internalSplitter = null;
      }

      config.save();
      splitter = createSplitter();

      if (splitter instanceof InternalSplitter) {
         internalSplitter = (InternalSplitter) splitter;
         renderTimeHandler.setSplitter(internalSplitter);
      }
   }

   public static double XSplitter;
   public static double YSplitter;

   private LiveSplitSplitter createSplitter() {
      config.load();
      XSplitter = config.get( Configuration.CATEGORY_CLIENT, "XSplitter",
              -1).getDouble();
      YSplitter = config.get( Configuration.CATEGORY_CLIENT, "YSplitter",
              -1).getDouble();
      String host = config.getString("host", Configuration.CATEGORY_GENERAL, "localhost",
              "The local IP to connect to LiveSplits");
      int port = config.getInt("port", Configuration.CATEGORY_GENERAL, -1, -1,
              65535, "The port to connect to LiveSplits");
      if (port == -1) {
         return new InternalSplitter(executor);
      }

      return new LiveSplitSocketSplitter(executor, host, port);
   }

   private boolean enabled = true;

   public boolean toggle() {
      return enabled = !enabled;
   }

   public double getXSplitter(){
      int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
      if(ZombiesAutoSplits.XSplitter < 0){
         return 1 - (double)this.fontRenderer.getStringWidth("0:00:0") / (double)screenWidth;
      }
      return XSplitter;
   }

   public double getYSplitter(){
      int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
      if(ZombiesAutoSplits.YSplitter < 0){
         return 1 - fontRenderer.FONT_HEIGHT / (double)screenHeight;
      }
      return YSplitter;
   }
}
