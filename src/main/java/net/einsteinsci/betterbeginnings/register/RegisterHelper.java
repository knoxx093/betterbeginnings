package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class RegisterHelper
{
	public static <T extends Item & IBBName> void registerItem(T item)
	{
		GameRegistry.registerItem(item, item.getName());
		RegisterItems.allItems.add(item);
	}

	public static <T extends Block & IBBName> void registerBlock(T block)
	{
		GameRegistry.registerBlock(block, block.getName());
		RegisterBlocks.allBlocks.add(block);
	}

	public static void registerSmelterOreRecipe(String input, String output, float experience, int gravel, int bonus)
	{
		for (ItemStack stack : OreDictionary.getOres(input))
		{
			List<ItemStack> valid = OreDictionary.getOres(output);
			if (!valid.isEmpty())
			{
				SmelterRecipeHandler.addRecipe(stack, OreDictionary.getOres(output).get(0),
				                               experience, gravel, bonus);
			}
		}
	}
}
