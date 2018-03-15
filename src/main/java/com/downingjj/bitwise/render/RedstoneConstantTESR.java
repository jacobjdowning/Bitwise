package com.downingjj.bitwise.render;

import com.downingjj.bitwise.ModBlocks;
import com.downingjj.bitwise.blocks.RedstoneConstant;
import com.downingjj.bitwise.tileentity.RedstoneConstantTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

/**
 * Created by Jacob on 13/03/2018.
 */

@SideOnly(Side.CLIENT)
public class RedstoneConstantTESR extends TileEntitySpecialRenderer<RedstoneConstantTile> {
    private static final double START_FROM_MIDDLE = 0.375D;
    private static final double CIRCUIT_HEIGHT = 0.125D;
    private static final double GAP = 0.25D;
    private static final double OFF_OFFSET = 0.125D;

    @Override
    public void render(RedstoneConstantTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        //PUSH
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderNubs(te);

        //POP
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }

    private void renderNubs(RedstoneConstantTile te) {
        //PUSH
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

        IBlockState state = ModBlocks.get("redstoneconstant").getDefaultState().withProperty(RedstoneConstant.NUBS, RedstoneConstant.EnumConstantModels.ON);
        IBakedModel modelON = dispatcher.getModelForState(state);

        state = ModBlocks.get("redstoneconstant").getDefaultState().withProperty(RedstoneConstant.NUBS, RedstoneConstant.EnumConstantModels.OFF);
        IBakedModel modelOFF = dispatcher.getModelForState(state);

        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(RedstoneConstant.FACING);
        GlStateManager.rotate(facing.getHorizontalAngle(), 0,1,0);

        double[] offsets = new double[3];

        offsets = getOffsets(offsets, facing);

        GlStateManager.translate(-te.getPos().getX() + offsets[0], -te.getPos().getY() + CIRCUIT_HEIGHT, -te.getPos().getZ() + offsets[1]);
//        GlStateManager.translate(-te.getPos().getX() - START_FROM_MIDDLE + (GAP * 3), -te.getPos().getY() + CIRCUIT_HEIGHT, -te.getPos().getZ() - OFF_OFFSET);

        int digit = 1;
        for(int i = 0; i<4; i++) {
            boolean wasOn = false;
            if((te.getPower() & digit) != 0){
                GlStateManager.translate(0,0,OFF_OFFSET * offsets[2]);
                wasOn = true;
            }
            digit <<= 1;
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            dispatcher.getBlockModelRenderer().renderModel(world, wasOn ? modelON : modelOFF, state, te.getPos(), bufferBuilder, true);
            tessellator.draw();
            GlStateManager.translate(-GAP * offsets[2],0,wasOn ? -OFF_OFFSET * offsets[2] : 0);
        }

        //POP
        GlStateManager.popMatrix();
    }

    private double[] getOffsets(double[] offsets, EnumFacing facing){
        switch(facing){
            case NORTH:
                offsets[0] = START_FROM_MIDDLE - 1;
                offsets[1] = - 1 - OFF_OFFSET;
                offsets[2] = 1;
                break;
            case EAST:
                offsets[0] = - (GAP * 1.5);
                offsets[1] = OFF_OFFSET - 1;
                offsets[2] = -1;
                break;
            case SOUTH:
                offsets[0] = -START_FROM_MIDDLE + (GAP * 3);
                offsets[1] = -OFF_OFFSET;
                offsets[2] = 1;
                break;
            case WEST:
                offsets[0] = - (GAP * 5.5);
                offsets[1] = OFF_OFFSET;
                offsets[2] = -1;
                break;

        }
        return offsets;
    }
}
