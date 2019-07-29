package com.tfar.angelblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AngelBlockMod.MODID)
public class AngelBlockMod
{
  // Directly reference a log4j logger.

  public static final String MODID = "angelblock";

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
      // register a new block here
      event.getRegistry().register(new AngelBlock(Block.Properties
              .create(Material.ROCK))
              .setRegistryName("angel_block"));
    }
    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
      // register a new item here
      event.getRegistry().register(new AngelBlockItem(Objects.angel_block,new Item.Properties()
              .group(ItemGroup.BUILDING_BLOCKS))
              .setRegistryName("angel_block"));
    }
  }
  @ObjectHolder(MODID)
  public static class Objects {
    public static final Block angel_block = null;
  }
}
