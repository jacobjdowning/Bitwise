package com.downingjj.bitwise;

import com.downingjj.bitwise.blocks.WiseBlock;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Jacob on 07/03/2018.
 */
public class ModBlocks {
    private static HashMap<String, WiseBlock> blocks;

    public static void createBlockMap(){
        blocks = new HashMap<String, WiseBlock>();

        //All mod blocks

    }

    public static WiseBlock get(String name){
        return blocks.get(name);
    }

    public static Collection<WiseBlock> getAll(){
        return blocks.values();
    }

    private static void add(WiseBlock block){
        blocks.put(block.getName(), block);
    }
}
