package iguanaman.iguanatweakstconstruct.harvestlevels;

import iguanaman.iguanatweakstconstruct.reference.Config;
import iguanaman.iguanatweakstconstruct.util.Log;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.tools.TinkerTools;

import static iguanaman.iguanatweakstconstruct.util.HarvestLevels.*;

/**
 * Used to modify the harvest levels and speeds of the tinker tool parts.
 * Has to be used together with the HarvestLevel Tweaks or everything will be messed up. :)
 */
public abstract class TinkerMaterialTweaks {
    private static float durabilityMod;
    private static float speedMod;

    public static void modifyToolMaterials()
    {
        Log.info("Modifying TConstruct materials");

        // save constant factor to apply to all durabilities and speeds
        durabilityMod = Config.durabilityPercentage / 100f;
        speedMod = Config.miningSpeedPercentage / 100f;

        // modify the base materials added by tinkers construct
        modifyTcon();
        // todo: modify mod parts.
        // modify ExtraTic/Metallurgy

        Log.info("Finished modifying TConstruct materials");
    }

    private static void modifyTcon()
    {
        // mining level 0: stone/wood
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Paper"),       21, 100, 1, 0.3f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Wood"),        50, 150, 1, 1.0f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Stone"),      100, 150, 1, 0.3f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Cactus"),     110, 500, 3, 1.0f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Netherrack"), 123, 456, 1, 1.2f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("BlueSlime"), 1200, 150, 1, 1.5f);
        updateMaterial(_0_stone, TConstructRegistry.getMaterial("Slime"),      500, 100, 1, 2.0f);

        // mining level 1: Flint and bone
        updateMaterial(_1_flint, TConstructRegistry.getMaterial("Flint"), 151, 400, 2, 0.5f);
        updateMaterial(_1_flint, TConstructRegistry.getMaterial("Bone"),  201, 375, 2, 1.08f);

        // mining level 2: Copper
        updateMaterial(_2_copper, TConstructRegistry.getMaterial("Copper"), 180, 500, 2, 1.1f);

        // mining level 3: Iron
        updateMaterial(_3_iron, TConstructRegistry.getMaterial("Iron"), 250, 600, 3, 1.2f);
        if(TinkerTools.thaumcraftAvailable)
            updateMaterial(_3_iron, TConstructRegistry.getMaterial("Thaumium"), 200, 650, 3, 1.18f);

        // mining level 4: Bronze and better metals
        int bronzeLevel = _4_bronze;
        if(!Config.nerfBronze)
            bronzeLevel++;
        updateMaterial(bronzeLevel, TConstructRegistry.getMaterial("Bronze"), 380, 650, 3, 1.25f);

        // mining level 5: diamond
        updateMaterial(_5_diamond, TConstructRegistry.getMaterial("Steel"),   400, 700, 3, 1.3f);
        updateMaterial(_5_diamond, TConstructRegistry.getMaterial("PigIron"), 667, 780, 3, 1.35f);

        // mining level 6: Obsidian and alumite
        updateMaterial(_6_obsidian, TConstructRegistry.getMaterial("Obsidian"), 89, 650, 2, 0.8f);
        updateMaterial(_6_obsidian, TConstructRegistry.getMaterial("Alumite"), 550, 790, 4, 1.3f);

        // mining level 7: Ardite
        updateMaterial(_7_ardite, TConstructRegistry.getMaterial("Ardite"), 606, 800, 4, 1.3f);

        // mining level 8: Cobalt
        updateMaterial(_8_cobalt, TConstructRegistry.getMaterial("Cobalt"), 800, 1100, 4, 1.75f);

        // mining level 9: Manyullyn
        updateMaterial(_9_manyullym, TConstructRegistry.getMaterial("Manyullyn"), 1200, 900, 5, 2.5f);
    }

    private static void updateMaterial(int harvestLevel, ToolMaterial old, int newDurability, int newSpeed, int newAttack, float newHandleModifier)
    {
        // construct modified material
        int durability = Math.round(newDurability * durabilityMod);
        int speed = Math.round(newSpeed * speedMod);
        // round everything
        ToolMaterial newMaterial = new ToolMaterial(old.materialName, old.displayName, harvestLevel, durability, speed, newAttack, newHandleModifier, old.reinforced, old.stonebound, old.tipStyle, old.ability);

        // replace old material
        Integer id = getMaterialID(old);
        if(id > -1) {
            TConstructRegistry.toolMaterials.remove(id);
            TConstructRegistry.toolMaterialStrings.remove(old.materialName);
            TConstructRegistry.addtoolMaterial(id, newMaterial);
        }
        else
            Log.error("Couldn't find ToolMaterial ID for " + old.name());
    }

    private static Integer getMaterialID(ToolMaterial material)
    {
        for(int k : TConstructRegistry.toolMaterials.keySet())
            if(TConstructRegistry.toolMaterials.get(k) == material)
                return k;

        return -1;
    }
}