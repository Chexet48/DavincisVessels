package darkevilmac.archimedes.client;

import darkevilmac.archimedes.ArchimedesShipMod;
import darkevilmac.archimedes.client.control.ShipKeyHandler;
import darkevilmac.archimedes.client.handler.ClientHookContainer;
import darkevilmac.archimedes.client.render.RenderParachute;
import darkevilmac.archimedes.client.render.TileEntityGaugeRenderer;
import darkevilmac.archimedes.client.render.TileEntityHelmRenderer;
import darkevilmac.archimedes.common.ArchimedesConfig;
import darkevilmac.archimedes.common.CommonProxy;
import darkevilmac.archimedes.common.entity.EntityParachute;
import darkevilmac.archimedes.common.entity.EntityShip;
import darkevilmac.archimedes.common.tileentity.TileEntityGauge;
import darkevilmac.archimedes.common.tileentity.TileEntityHelm;
import darkevilmac.movingworld.client.render.RenderMovingWorld;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoaderState;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {
    public ShipKeyHandler shipKeyHandler;

    public ArchimedesConfig syncedConfig;

    @Override
    public ClientHookContainer getHookContainer() {
        return new ClientHookContainer();
    }

    @Override
    public void registerKeyHandlers(ArchimedesConfig cfg) {
        shipKeyHandler = new ShipKeyHandler(cfg);
        FMLCommonHandler.instance().bus().register(shipKeyHandler);
    }

    @Override
    public void registerRenderers(LoaderState.ModState state) {
        if (state == LoaderState.ModState.PREINITIALIZED) {
            registerRendererVariants();
        }

        if (state == LoaderState.ModState.INITIALIZED) {
            registerEntityRenderers();
            registerTileEntitySpeacialRenderers();
            registerItemRenderers();
        }

        if (state == LoaderState.ModState.POSTINITIALIZED) {

        }
    }

    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityShip.class, new RenderMovingWorld(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityParachute.class, new RenderParachute(Minecraft.getMinecraft().getRenderManager()));
    }

    public void registerTileEntitySpeacialRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGauge.class, new TileEntityGaugeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHelm.class, new TileEntityHelmRenderer());
    }

    public void registerRendererVariants() {
        Item itemToRegister = null;
        ArrayList<String> variants = null;

        itemToRegister = Item.getItemFromBlock(ArchimedesShipMod.objects.blockBalloon);
        variants = new ArrayList<String>();

        for (EnumDyeColor color : EnumDyeColor.values()) {
            variants.add(ArchimedesShipMod.RESOURCE_DOMAIN + "balloon_" + color.getName());
        }

        String[] variantsArray = new String[variants.size()];
        int index = 0;

        for (String str : variants) {
            variantsArray[index] = str;
            index++;
        }

        ModelBakery.addVariantName(itemToRegister, variantsArray);

        ModelBakery.addVariantName(Item.getItemFromBlock(ArchimedesShipMod.objects.blockGauge), ArchimedesShipMod.RESOURCE_DOMAIN + "gauge", ArchimedesShipMod.RESOURCE_DOMAIN + "gauge_ext");
    }

    public void registerItemRenderers() {
        Item itemToRegister = null;
        ModelResourceLocation modelResourceLocation = null;

        ItemModelMesher modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        // Do some general render registrations for objects, not considering meta.

        for (int i = 0; i < ArchimedesShipMod.objects.registeredBlocks.size(); i++) {
            modelResourceLocation = new ModelResourceLocation(ArchimedesShipMod.RESOURCE_DOMAIN + ArchimedesShipMod.objects.registeredBlocks.keySet().toArray()[i], "inventory");
            itemToRegister = Item.getItemFromBlock((Block) ArchimedesShipMod.objects.registeredBlocks.values().toArray()[i]);

            modelMesher.register(itemToRegister, 0, modelResourceLocation);
        }

        // Some specific meta registrations for objects, like for extended gauges.
        itemToRegister = Item.getItemFromBlock(ArchimedesShipMod.objects.blockGauge);
        modelResourceLocation = new ModelResourceLocation(ArchimedesShipMod.RESOURCE_DOMAIN + "gauge_ext", "inventory");

        modelMesher.register(itemToRegister, 1, modelResourceLocation);

        itemToRegister = Item.getItemFromBlock(ArchimedesShipMod.objects.blockBalloon);
        modelResourceLocation = null;

        for (EnumDyeColor color : EnumDyeColor.values()) {
            modelResourceLocation = new ModelResourceLocation(ArchimedesShipMod.RESOURCE_DOMAIN + "balloon_" + color.getName(), "inventory");
            modelMesher.register(itemToRegister, color.getMetadata(), modelResourceLocation);
        }
    }

}
