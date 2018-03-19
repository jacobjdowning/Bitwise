package com.downingjj.bitwise.blocks;

import com.downingjj.bitwise.util.Util;
import com.google.common.util.concurrent.ExecutionError;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
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

    public enum EnumOperation implements IStringSerializable {
        AND(0, "and"),
        OR(1, "or"),
        XOR(2, "xor");

        private String name;
        private int meta;
        EnumOperation(int meta, String name){
            this.meta = meta;
            this.name = name;
        }
        @Override
        public String getName() {
            return this.name;
        }
        public int getMeta(){ return this.meta; }

        public static EnumOperation getFromMeta(int meta){
            switch (meta){
                case 0:
                    return EnumOperation.AND;
                case 1:
                    return EnumOperation.OR;
                case 2:
                    return EnumOperation.XOR;
                default:
                    throw new RuntimeException("Meta out of range for Bitwise gate operation");
            }
        }
    }
    public static final IProperty<EnumOperation> OP = PropertyEnum.create("op", EnumOperation.class);

    private int power;
    int[] inputs = new int[2];

    public BitwiseGate() {
        super(NAME, Material.CIRCUITS);
        setDefaultState(super.getDefaultState().withProperty(OP, EnumOperation.AND));
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

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if(player.isSneaking() & player.getHeldItem(hand).isEmpty()){
            int meta = state.getValue(OP).getMeta();
            meta += 1;
            meta = meta < 3 ? meta : 0;
            IBlockState newState = state.withProperty(OP, EnumOperation.getFromMeta(meta));
            world.setBlockState(pos, newState);

            power = performOperation(inputs[0], inputs[1], newState);
            world.notifyNeighborsOfStateChange(pos, this, false);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing blockFacing = (EnumFacing)state.getValue(FACING);
        EnumFacing[] inputFaces = new EnumFacing[2];

        inputFaces[0] = blockFacing.rotateY();
        inputFaces[1] = inputFaces[0].getOpposite();

        super.onBlockAdded(worldIn, pos, state);

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

        power =  performOperation(inputs[0], inputs[1], state);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
        if(this.canBlockStay(worldIn, pos)){
            EnumFacing blockFacing = (EnumFacing) state.getValue(FACING);
            EnumFacing[] inputFaces = new EnumFacing[2];
            EnumFacing changedFace = Util.getFacingFromPositions(pos, fromPos);

            inputFaces[0] = blockFacing.rotateY();
            inputFaces[1] = inputFaces[0].getOpposite();

            if (changedFace.compareTo(inputFaces[0]) == 0 || changedFace.compareTo(inputFaces[1]) == 0) {

                for (int i = 0; i < 2; i++) {
                    BlockPos blockpos = pos.offset(inputFaces[i]);
                    int power = worldIn.getRedstonePower(blockpos, inputFaces[i]);

                    if (power >= 15) {
                        inputs[i] = 15;
                    } else {
                        IBlockState iblockstate = worldIn.getBlockState(blockpos);
                        inputs[i] = Math.max(power, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? (iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
                    }
                }

                int newPow = performOperation(inputs[0], inputs[1], state);
                if (newPow != power) {
                    power = newPow;
                    worldIn.notifyNeighborsOfStateChange(pos, this, false);
                }
            }
        }else{
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    private int performOperation(int a, int b, IBlockState state){
        EnumOperation op = state.getValue(OP);
        switch (op){
            case AND:
                return a & b;
            case OR:
                return a | b;
            case XOR:
            default:
                return a ^ b;
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid();
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

    @Override
    public IBlockState getStateFromMeta(int meta){
        return super.getStateFromMeta(meta).withProperty(OP, EnumOperation.getFromMeta((meta & 12) >> 2));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        int opMeta = state.getValue(OP).getMeta();
        return super.getMetaFromState(state) + (opMeta << 2);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, OP);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
    }




}
