package com.downingjj.bitwise.proxy;

        import com.downingjj.bitwise.ModBlocks;
        import com.downingjj.bitwise.Reference;
        import com.downingjj.bitwise.blocks.WiseBlock;
        import net.minecraft.block.Block;
        import net.minecraft.block.ITileEntityProvider;
        import net.minecraft.item.Item;
        import net.minecraft.item.ItemBlock;
        import net.minecraftforge.event.RegistryEvent;
        import net.minecraftforge.fml.common.Mod;
        import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
        import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
        import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jacob on 07/03/2018.
 */
@Mod.EventBusSubscriber
public abstract class CommonProxy implements IProxy {
    public void preInit(FMLPreInitializationEvent event){
        ModBlocks.createBlockMap();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        for (WiseBlock b : ModBlocks.getAll()){
            event.getRegistry().register(b);
            if(b instanceof ITileEntityProvider){
                GameRegistry.registerTileEntity(((ITileEntityProvider) b).createNewTileEntity(null, 0).getClass(), Reference.MOD_ID + "_" + b.getName());
            }
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for(WiseBlock b : ModBlocks.getAll()){
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));

        }
    }
}
