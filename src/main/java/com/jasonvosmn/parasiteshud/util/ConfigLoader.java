package com.jasonvosmn.parasiteshud.util;


import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.jasonvosmn.parasiteshud.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;


public class ConfigLoader {
    public static int[] pointsForStage = new int[11];

    public static byte getPhase(EntityPlayer player) {
        World world = player.getEntityWorld();
        int id = world.provider.getDimension();
        return SRPSaveData.get(world).getEvolutionPhase(id);
    }

    public static int getTotalPoints (EntityPlayer player) {
        World world = player.getEntityWorld();
        int id = world.provider.getDimension();
        return SRPSaveData.get(world).getTotalKills(id);
    }

    public static int getPointsNextPhase(byte phase) {
        if (phase == ConfigLoader.pointsForStage.length) {
            return 0;
        } else if (phase < 0) {
            return ConfigLoader.pointsForStage[10];
        } else {
            return ConfigLoader.pointsForStage[phase];
        }
    }

    public static int getCooldown (EntityPlayer player){
        World world = player.getEntityWorld();
        int id = world.provider.getDimension();
        return SRPSaveData.get(world).getCooldown(world,id);
    }

    public static void initConfig(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        CommonProxy.configsystems = new Configuration(new File(configDir.getPath(), "srparasites/SRParasitesSystems.cfg"));
        readConfig();
    }

    public static void readConfig() {
        Configuration config = CommonProxy.configsystems;
        config.load();

        for (int i = 0; i < 10; i++) {
            pointsForStage[i] = config.getInt("Phase " + String.valueOf(i + 1) + " Points",
                    Reference.StageNames.ALL_STAGES[i], 0, 1, Integer.MAX_VALUE,
                    "Sets the required number of points for " + Reference.StageNames.ALL_STAGES[i]);

            Logger.info(String.format("Phase %d: %d points required (loaded from config)",
                    i + 1, pointsForStage[i]));
        }
        pointsForStage[10] = 0;
        Logger.info(String.format("Phase %d: %d points required (loaded from config)",
                -1, pointsForStage[10]));

        config.save();
    }
}
