package val_int1.ingore.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.GenerationStep;

public class IngOreInitCommon implements ModInitializer {

	public static final String MODID = "ingore";
	public static final String MOD_NAME = "Ing Ore";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	private static final FabricItemSettings SETTINGS = new FabricItemSettings();
	private static final IntProvider XP_PROVIDER = UniformIntProvider.create(0, 2);
	
	public static final Block ING_ORE = new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.COAL_ORE), XP_PROVIDER);
	public static final Block DEEPSLATE_ING_ORE = new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_COAL_ORE), XP_PROVIDER);
	public static final Item ING = new Item(SETTINGS);
	public static final Item GNI = new Item(SETTINGS);
	
	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier(MODID, "ing_ore"), ING_ORE);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "deepslate_ing_ore"), DEEPSLATE_ING_ORE);
		
		Registry.register(Registries.ITEM, new Identifier(MODID, "ing_ore"), new BlockItem(ING_ORE, SETTINGS));
		Registry.register(Registries.ITEM, new Identifier(MODID, "deepslate_ing_ore"), new BlockItem(DEEPSLATE_ING_ORE, SETTINGS));
		Registry.register(Registries.ITEM, new Identifier(MODID, "ing"), ING);
		Registry.register(Registries.ITEM, new Identifier(MODID, "gni"), GNI);
		
		BiomeModifications.create(new Identifier(MODID, "spawn_ing_ores"))
			.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), IngOreInitCommon::spawnIngOres);
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(IngOreInitCommon::addOresToCreative);
	}
	
	private static final void spawnIngOres(BiomeModificationContext ctx) {
		ctx.getGenerationSettings().addFeature(GenerationStep.Feature.RAW_GENERATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MODID, "ing_ore_upper")));
		ctx.getGenerationSettings().addFeature(GenerationStep.Feature.RAW_GENERATION,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MODID, "ing_ore_lower")));
	}
	
	private static final void addOresToCreative(FabricItemGroupEntries entries) {
		entries.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, Lists.newArrayList(
			new ItemStack(ING_ORE), new ItemStack(DEEPSLATE_ING_ORE) 
		));
	}

}
