package fx7.r2m.rest.parameter.location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;
import fx7.r2m.rest.parameter.RestParameter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.ListTag;

public class LocationParameter implements RestParameter, MinecraftParameter<Location>
{
	private final String world;
	private final double x;
	private final double y;
	private final double z;

	private LocationParameter(String world, double x, double y, double z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Location toMinecraftParameter(Set<EntityAccess> entityAccess) throws RestException
	{
		return LocationParameterProvider.fromParameters(entityAccess, world, x, y, z);
	}

	@Override
	public String toString()
	{
		return "LocationEntity [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public static LocationParameter fromLocation(Location location)
	{
		return new LocationParameter(//
				location.getWorld().getName(), //
				location.getX(), //
				location.getY(), //
				location.getZ());
	}

	public static Location getLocationFromOfflinePlayer(OfflinePlayer player)
	{
		UUID uuid = player.getUniqueId();

		Map<World, File> worldPlayerFile = new HashMap<>();
		for (World w : Bukkit.getWorlds())
		{
			File[] playerFiles = null;
			File worldFolder = w.getWorldFolder();
			File[] playerDataFolder = worldFolder.listFiles((p) -> p.isDirectory() && "playerdata".equals(p.getName()));
			if (playerDataFolder != null && playerDataFolder.length == 1)
				playerFiles = playerDataFolder[0]
						.listFiles((p) -> p.isFile() && p.getName().equals(uuid.toString() + ".dat"));
			if (playerFiles != null && playerFiles.length == 1)
				worldPlayerFile.put(w, playerFiles[0]);
		}

		// TODO MultiWorld / Nether / End ?
		// TODO Exception
		for (Entry<World, File> entry : worldPlayerFile.entrySet())
		{
			try
			{
				World world = entry.getKey();
				File file = entry.getValue();
				NamedTag root = NBTUtil.read(file);
				CompoundTag tag = (CompoundTag) root.getTag();
				ListTag<DoubleTag> pos = (ListTag<DoubleTag>) tag.get("Pos");
				double x = pos.get(0).asDouble();
				double y = pos.get(1).asDouble();
				double z = pos.get(2).asDouble();
				return new Location(world, x, y, z);
			} catch (Exception e)
			{
			}
		}

		return null;
	}
}
