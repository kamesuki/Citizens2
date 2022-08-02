package net.citizensnpcs.trait;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.ZombieVillager;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

/**
 * Persists the Villager profession metadata.
 *
 * @see Profession
 */
@TraitName("profession")
public class VillagerProfession extends Trait {
    private Profession profession = Profession.FARMER;

    public VillagerProfession() {
        super("profession");
    }

    public Profession getProfession() {
        return profession;
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        try {
            profession = Profession.valueOf(key.getString(""));
            if ("NORMAL".equals(profession.name())) {
                profession = Profession.FARMER;
            }
        } catch (IllegalArgumentException ex) {
            throw new NPCLoadException("Invalid profession.");
        }
    }

    @Override
    public void onSpawn() {
        if (!npc.isSpawned())
            return;
        if (npc.getEntity() instanceof Villager) {
            ((Villager) npc.getEntity()).setProfession(profession);
            return;
        }
        if (SUPPORT_ZOMBIE_VILLAGER) {
            try {
                if (npc.getEntity() instanceof ZombieVillager) {
                    ((ZombieVillager) npc.getEntity()).setVillagerProfession(profession);
                }
            } catch (Throwable t) {
                SUPPORT_ZOMBIE_VILLAGER = false;
            }
        }
    }

    @Override
    public void save(DataKey key) {
        key.setString("", profession.name());
    }

    public void setProfession(Profession profession) {
        if ("NORMAL".equals(profession.name())) {
            profession = Profession.FARMER;
        }
        this.profession = profession;
        onSpawn();
    }

    @Override
    public String toString() {
        return "Profession{" + profession + "}";
    }

    private static boolean SUPPORT_ZOMBIE_VILLAGER = true;
}