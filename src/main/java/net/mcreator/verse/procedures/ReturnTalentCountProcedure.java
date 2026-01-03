package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnTalentCountProcedure {
    public static int execute(Entity entity) {
        if (entity == null)
            return 0;
        
        String talentList = entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist;
        
        // Count the number of ")" characters
        return (int) talentList.chars().filter(ch -> ch == ')').count();
    }
}