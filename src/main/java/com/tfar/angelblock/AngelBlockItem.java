package com.tfar.angelblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class AngelBlockItem extends BlockItem {
  public AngelBlockItem(Block p_i48527_1_, Properties p_i48527_2_) {
    super(p_i48527_1_, p_i48527_2_);
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player,@Nonnull Hand hand) {

    if (!world.isRemote){
      double x = player.getPositionVec().x + 4 * player.getLookVec().x;
      double y = player.getEyeHeight() + player.getPositionVec().y  + 4 * player.getLookVec().y;
      double z = player.getPositionVec().z  + 4 * player.getLookVec().z;

      BlockPos pos = new BlockPos(x,y,z);

      if (world.isAirBlock(pos)) {
        world.setBlockState(pos, AngelBlockMod.angel_block.getDefaultState());
        if (!player.abilities.isCreativeMode)
          player.getHeldItem(hand).shrink(1);
      }
    }
    return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
  }
}
