package com.tfar.angelblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.util.math.RayTraceResult.Type.MISS;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class Render {

	public static final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void highlight(RenderWorldLastEvent e) {
		if (mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof AngelBlockItem
						&& mc.objectMouseOver instanceof BlockRayTraceResult
						&& mc.objectMouseOver.getType() == MISS) {
			PlayerEntity player = mc.player;
			World world = player.world;
			double x = player.getPositionVec().x + 4 * player.getLookVec().x;
			double y = player.getEyeHeight() + player.getPositionVec().y + 4 * player.getLookVec().y;
			double z = player.getPositionVec().z + 4 * player.getLookVec().z;

			BlockPos pos = new BlockPos(x, y, z);

			boolean valid = world.getFluidState(pos).isEmpty();
			if (world.isAirBlock(pos)) {
				renderBlock(e, pos, valid ? 0x00ff00 : 0xff0000);
			}
		}
	}

	private static final int GL_FRONT_AND_BACK = 1032;
	private static final int GL_LINE = 6913;
	private static final int GL_FILL = 6914;
	private static final int GL_LINES = 1;

	public static void renderBlock(RenderWorldLastEvent e, BlockPos pos, int color) {

		Vector3d vec3d = TileEntityRendererDispatcher.instance.renderInfo.getProjectedView();

		MatrixStack stack = e.getMatrixStack();
		stack.translate(-vec3d.x, -vec3d.y, -vec3d.z);

		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(stack.getLast().getMatrix());

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		Profile.BLOCKS.apply(); // Sets GL state for block drawing

		buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		renderBlockBounding(buffer, pos, color, 1);
		tessellator.draw();
		Profile.BLOCKS.clean();
		RenderSystem.popMatrix();
	}

	private static void renderBlockBounding(BufferBuilder buffer, BlockPos pos, int color, int opacity) {

		final float size = 1.0f;

		float red = (color >> 16 & 0xff) / 255f;
		float green = (color >> 8 & 0xff) / 255f;
		float blue = (color & 0xff) / 255f;


		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// TOP
		buffer.pos(x, y + size, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y + size, z).color(red, green, blue, opacity).endVertex();

		// BOTTOM
		buffer.pos(x + size, y, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y, z).color(red, green, blue, opacity).endVertex();

		// Edge 1
		buffer.pos(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();

		// Edge 2
		buffer.pos(x + size, y, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x + size, y + size, z).color(red, green, blue, opacity).endVertex();

		// Edge 3
		buffer.pos(x, y, z + size).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y + size, z + size).color(red, green, blue, opacity).endVertex();

		// Edge 4
		buffer.pos(x, y, z).color(red, green, blue, opacity).endVertex();
		buffer.pos(x, y + size, z).color(red, green, blue, opacity).endVertex();
	}

	/**
	 * OpenGL Profiles used for rendering blocks and entities
	 */
	private enum Profile {
		BLOCKS {
			@Override
			public void apply() {
				RenderSystem.disableTexture();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.polygonMode(GL_FRONT_AND_BACK, GL_LINE);
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				RenderSystem.enableBlend();
				RenderSystem.lineWidth(1);
			}

			@Override
			public void clean() {
				RenderSystem.polygonMode(GL_FRONT_AND_BACK, GL_FILL);
				RenderSystem.disableBlend();
				RenderSystem.enableDepthTest();
				RenderSystem.depthMask(true);
				RenderSystem.enableTexture();
			}
		};

		Profile() {
		}

		public abstract void apply();

		public abstract void clean();
	}

}
