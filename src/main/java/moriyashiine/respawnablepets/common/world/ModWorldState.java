/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.respawnablepets.common.world;

import moriyashiine.respawnablepets.common.RespawnablePets;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModWorldState extends PersistentState {
	public final List<NbtCompound> storedPets = new ArrayList<>();
	public final List<UUID> petsToRespawn = new ArrayList<>();

	public static ModWorldState readNbt(NbtCompound nbt) {
		ModWorldState worldState = new ModWorldState();
		NbtList storedPets = nbt.getList("StoredPets", NbtType.COMPOUND);
		for (int i = 0; i < storedPets.size(); i++) {
			worldState.storedPets.add(storedPets.getCompound(i));
		}
		NbtList petsToRespawn = nbt.getList("PetsToRespawn", NbtType.STRING);
		for (int i = 0; i < petsToRespawn.size(); i++) {
			worldState.petsToRespawn.add(UUID.fromString(petsToRespawn.getString(i)));
		}
		return worldState;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList storedPets = new NbtList();
		storedPets.addAll(this.storedPets);
		nbt.put("StoredPets", storedPets);
		NbtList petsToRespawn = new NbtList();
		for (UUID uuid : this.petsToRespawn) {
			petsToRespawn.add(NbtString.of(uuid.toString()));
		}
		nbt.put("PetsToRespawn", petsToRespawn);
		return nbt;
	}

	@SuppressWarnings("ConstantConditions")
	public static ModWorldState get(World world) {
		return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(ModWorldState::readNbt, ModWorldState::new, RespawnablePets.MOD_ID);
	}
}