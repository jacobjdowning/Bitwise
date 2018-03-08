package com.downingjj.bitwise.blocks;

import net.minecraft.block.material.Material;

/**
 * Created by Jacob on 07/03/2018.
 */
public class RedstoneConstant extends HorizontalWise {
    private static final String NAME = "redstoneconstant";

    public RedstoneConstant() {
        super(NAME, Material.CIRCUITS);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
