package com.downingjj.bitwise.blocks;

import com.downingjj.bitwise.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Jacob on 14/03/2018.
 */
public class BitwiseGate extends HorizontalWise {
    private static final String NAME = "bitwisegate";
    protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    private int power;

    public BitwiseGate() {
        super(NAME, Material.CIRCUITS);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return REDSTONE_DIODE_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }


    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
        return true;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
        EnumFacing blockFacing = (EnumFacing)state.getValue(FACING);
        EnumFacing[] inputFaces = new EnumFacing[2];
        EnumFacing changedFace = Util.getFacingFromPositions(pos, fromPos);

        inputFaces[0] = blockFacing.rotateY();
        inputFaces[1] = inputFaces[0].getOpposite();

        if(changedFace.compareTo(inputFaces[0]) == 0 || changedFace.compareTo(inputFaces[1]) == 0){
            int[] inputs = new int[2];

            for(int i = 0; i < 2; i++){
                BlockPos blockpos = pos.offset(inputFaces[i]);
                int power = worldIn.getRedstonePower(blockpos, inputFaces[i]);

                if(power >= 15){
                    inputs[i] = 15;
                }else{
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    inputs[i] = Math.max(power, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? (iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
                }
            }

            int newPow = inputs[0] & inputs[1];
            if(newPow != power){
                power = newPow;
                worldIn.notifyNeighborsOfStateChange(pos, this, false);
            }
        }
    }

    private int getPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face){
        if(face.compareTo(state.getValue(FACING).getOpposite()) == 0) {
            return power;
        }
        return 0;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face){
        return getPower(state, world, pos, face);
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face){
        return getPower(state, world, pos, face);
    }
}
