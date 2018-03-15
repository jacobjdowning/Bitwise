package com.downingjj.bitwise.blocks;

        import com.downingjj.bitwise.render.RedstoneConstantTESR;
        import com.downingjj.bitwise.tileentity.RedstoneConstantTile;
        import com.downingjj.bitwise.util.Util;
        import net.minecraft.block.ITileEntityProvider;
        import net.minecraft.block.material.Material;
        import net.minecraft.block.properties.IProperty;
        import net.minecraft.block.properties.PropertyEnum;
        import net.minecraft.block.state.BlockStateContainer;
        import net.minecraft.block.state.IBlockState;
        import net.minecraft.entity.player.EntityPlayer;
        import net.minecraft.tileentity.TileEntity;
        import net.minecraft.util.BlockRenderLayer;
        import net.minecraft.util.EnumFacing;
        import net.minecraft.util.EnumHand;
        import net.minecraft.util.IStringSerializable;
        import net.minecraft.util.math.AxisAlignedBB;
        import net.minecraft.util.math.BlockPos;
        import net.minecraft.world.IBlockAccess;
        import net.minecraft.world.World;
        import net.minecraftforge.fml.client.registry.ClientRegistry;
        import net.minecraftforge.fml.relauncher.Side;
        import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jacob on 07/03/2018.
 */
public class RedstoneConstant extends HorizontalWise implements ITileEntityProvider{
    private static final String NAME = "redstoneconstant";
    protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    //for reference to json models
    public enum EnumConstantModels implements IStringSerializable{
        BLOCK("block"),
        OFF("off"),
        ON("on");

        private String name;
        EnumConstantModels(String name){
            this.name = name;
        }
        @Override
        public String getName() {
            return this.name;
        }
    }
    public static final IProperty<EnumConstantModels> NUBS = PropertyEnum.create("nubs", EnumConstantModels.class);

    public RedstoneConstant() {
        super(NAME, Material.CIRCUITS);
        setDefaultState(super.getDefaultState().withProperty(NUBS, EnumConstantModels.BLOCK));
    }

    @Override
    public String getName() {
        return NAME;
    }

    private int getPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face){
        if(face.compareTo(state.getValue(FACING).getOpposite()) == 0){
            return Util.getTEfromPos(world,pos, RedstoneConstantTile.class).getPower();
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new RedstoneConstantTile();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {

        if(player.isSneaking()) {
            Util.getTEfromPos(world, pos, RedstoneConstantTile.class).cycle();
        }else if(side.compareTo(EnumFacing.UP)==0){
            EnumFacing blockDirection = state.getValue(FACING);
            Util.getTEfromPos(world, pos, RedstoneConstantTile.class).hitDigit(getDigitfromHit(hitX, hitZ, blockDirection));
        }else{
            return false;
        }

        world.notifyNeighborsOfStateChange(pos, this, false);
        return true;
    }

    private int getDigitfromHit(float hitX, float hitZ, EnumFacing facing){
        float hit;
        switch(facing){
            case NORTH:
                hit = hitX;
                break;
            case EAST:
                hit = hitZ;
                break;
            case SOUTH:
                hit = 1 - hitX;
                break;
            case WEST:
                hit = 1 - hitZ;
                break;
            default:
                hit = hitX;
                break;
        }
        return (int) Math.pow(2,Math.floor((hit) * 4));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return REDSTONE_DIODE_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(){
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(RedstoneConstantTile.class, new RedstoneConstantTESR());
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
        return state.getValue(FACING).getOpposite().compareTo(side) == 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos).withProperty(NUBS, EnumConstantModels.BLOCK);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, NUBS);
    }


    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
