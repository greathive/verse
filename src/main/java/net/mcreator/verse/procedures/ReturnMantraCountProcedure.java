package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

public class ReturnMantraCountProcedure {
	public static int execute(Entity entity, String mantras) {
        if (entity == null)
            return 0;
        
        String talentList = mantras;
        
        // Count the number of ")" characters
        return (int) talentList.chars().filter(ch -> ch == '[').count();
    }
}