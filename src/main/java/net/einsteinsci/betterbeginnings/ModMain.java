package net.einsteinsci.betterbeginnings;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.event.BBEventHandler;
import net.einsteinsci.betterbeginnings.event.Worldgen;
import net.einsteinsci.betterbeginnings.network.ServerProxy;
import net.einsteinsci.betterbeginnings.register.*;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

@Mod(modid = ModMain.MODID, version = ModMain.VERSION, name = ModMain.NAME,
     guiFactory = "net.einsteinsci.betterbeginnings.config.BBConfigGuiFactory")
public class ModMain
{
	public static final String MODID = "betterbeginnings";
	public static final String VERSION = "0.0.6.0";
	public static final String NAME = "Better Beginnings";
	public static final int PACKET_REPAIR_TABLE_REPAIR = 0;
	public static final CreativeTabs tabBetterBeginnings = new CreativeTabs("tabBetterBeginnings")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return RegisterItems.flintKnife;
		}
	};
	public static final AchievementPage pageBetterBeginnings = new AchievementPage("Better Beginnings",
	                                                                               RegisterAchievements
			                                                                               .getAchievements());

	@Instance(ModMain.MODID)
	public static ModMain modInstance;
	public static Configuration configFile;
	public static SimpleNetworkWrapper network;
	@SidedProxy(clientSide = "net.einsteinsci.betterbeginnings.network.ClientProxy",
	            serverSide = "net.einsteinsci.betterbeginnings.network.ServerProxy")
	public static ServerProxy proxy;
	public BBEventHandler eventHandler = new BBEventHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		Log("Starting pre-initialization...");

		configFile = new Configuration(e.getSuggestedConfigurationFile());
		configFile.load();
		BBConfig.syncConfig(configFile);

		proxy.registerNetworkStuff();
		proxy.registerRenderThings();

		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);

		network = NetworkRegistry.INSTANCE.newSimpleChannel("betterbeginnings");

		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTileEntities.register();

		// armorMaterialCloth.customCraftingMaterial = RegisterItems.cloth;
	}

	public static void Log(String text)
	{
		Log(Level.INFO, text);
	}

	public static void Log(Level level, String text)
	{
		FMLLog.log("Better Beginnings", level, text);
	}

	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addAdvancedRecipes();
		RegisterRecipes.addFurnaceRecipes();
		Worldgen.addWorldgen();

		AchievementPage.registerAchievementPage(pageBetterBeginnings);
		RemoveRecipes.removeFurnaceRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		RegisterItems.tweakVanilla();
	}
}
