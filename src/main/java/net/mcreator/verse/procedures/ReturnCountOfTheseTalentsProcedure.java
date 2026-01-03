package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;

public class ReturnCountOfTheseTalentsProcedure {
	public static int execute(Entity entity, String talents) {
        if (entity == null)
            return 0;
        
        String talentList = talents;
        
        // Count the number of ")" characters
        return (int) talentList.chars().filter(ch -> ch == ')').count();
    }
}