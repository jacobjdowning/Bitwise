package com.downingjj.bitwise.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Jacob on 12/03/2018.
 */
public class RedstoneConstantTile extends TileEntity{
    private int power = 0;

    public int cycle(){
        power ++;
        if(power > 15){
            power = 0;
        }
        markDirty();
        if(world != null){
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
        return power;
    }

    public int hitDigit(int digit){
        return power ^= digit;
    }

    public int getPower(){
        return power;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        if(compound.hasKey("power")){
            power = compound.getInteger("power");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        compound.setInteger("power", power);
        return compound;
    }
}
