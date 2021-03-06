package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.lireherz.guidebook.ConfigValues;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ArcaneArchives.MODID)
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler
{
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(ArcaneArchives.MODID)) {
			parseColours();
		}
	}

	@Config.Ignore
	public static int MANIFEST_HIGHLIGHT = 0x991922c4;

	public static void parseColours () {
		try {
			MANIFEST_HIGHLIGHT = Long.decode(ChestHighlight.toLowerCase()).intValue();
		} catch (NumberFormatException event) {
			ArcaneArchives.logger.error("Invalid manifest highlight colour: " + ChestHighlight, event);
		}
	}

	@Config.Comment("Limit of resonators per player's network")
	@Config.Name("Resonator Limit")
	public static int ResonatorLimit = 3;

	@Config.Comment("Limit of matrix cores per player's network")
	@Config.Name("Matrix Core Limit")
	public static int MatrixCoreLimit = 1;

	@Config.Comment("Number of ticks it takes to create raw quartz")
	@Config.Name("Resonator Time")
	public static int ResonatorTickTime = 6000;

	@Config.Comment("Whether having a manifest in your inventory is required to open the screen")
	@Config.Name("Manifest Presence")
	public static boolean ManifestPresence = true;

	@Config.Comment("Radiant chest highlight colour (use HTML syntax i.e., #FFFFFF")
	@Config.Name("Radiant Chest Highlight")
	public static String ChestHighlight = "#1922C4";

	@Config.Comment("Causes Radiant Chests, Troves, etc, to be unbreakable when not empty")
	@Config.Name("Unbreakable Radiant Chests & Troves")
	public static boolean UnbreakableContainers = false;

	@Config.Comment("Tome of Arcana settings (from Guidebook)")
	@Config.Name("Tome of Arcana settings")
	public static ConfigValues ConfigValues = new ConfigValues();

	@Config.Comment("Disable to use default Minecraft-style GUI elements. (Client Only)")
	@Config.Name("Use Pretty GUIs")
	public static boolean UsePrettyGUIs = true;

	@Config.Comment("Disable overlay of grid on Manifest. (Client Only")
	@Config.Name("Disable Manifest Grid")
	public static boolean DisableManifestGrid = true;

	//public static boolean bJarvisModeEnabled = false;

	/*public static int iRadiantResonatorDrain = 20;
	public static int iRepositoryMatrixDrain = 80;
	public static int iStorageMatrixDrain = 80;*/

	/*@Config.Comment("The Amount of Bonus Ticks Blocks Get if Their Drain is Met")
	public static int iRadiantResonatorBonusTicks = 1;*/

	/*@Config.Comment("The Amount of Items Each Repository Can Hold")
	public static int iRepositoryMatrixItemCap = 256;
	public static int iStorageMatrixItemCap = 256;*/
}
