package fx7.r2m.rest.parameter.location;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;
import fx7.r2m.rest.parameter.RestParameter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

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
		try
		{
			NamedTag playerFile = getPlayerDats(player);
			if (playerFile == null)
				return null;

			// TODO Exception
			CompoundTag tag = (CompoundTag) playerFile.getTag();
			World world = getWorldFromDimensionTag((Tag<String>) tag.get("Dimension"));
			ListTag<DoubleTag> pos = (ListTag<DoubleTag>) tag.get("Pos");
			double x = pos.get(0).asDouble();
			double y = pos.get(1).asDouble();
			double z = pos.get(2).asDouble();
			return new Location(world, x, y, z);
		} catch (Exception e)
		{
		}

		return null;
	}

	private static World getWorldFromDimensionTag(Tag<String> dimension)
	{
		// TODO ob das so stimmt ...
		Environment env = null;
		String[] dimensionSplit = dimension.valueToString().replaceAll("\"", "").split(":");
		if ("overworld".equals(dimensionSplit[1]))
			env = Environment.NORMAL;
		else if (Environment.NETHER.name().toLowerCase().equals(dimensionSplit[1]))
			env = Environment.NETHER;
		else if (Environment.THE_END.name().toLowerCase().equals(dimensionSplit[1]))
			env = Environment.THE_END;

		// no environment from dimension => its the name of the world!
		if (env == null)
			Bukkit.getWorld(dimensionSplit[1]);

		for (World world : Bukkit.getWorlds())
		{
			if (world.getEnvironment() == env)
				return world;
		}

		return null;
	}

	private static NamedTag getPlayerDats(OfflinePlayer player) throws IOException
	{
		UUID uuid = player.getUniqueId();

		for (World w : Bukkit.getWorlds())
		{
			File[] playerFiles = null;
			File worldFolder = w.getWorldFolder();
			File[] playerDataFolder = worldFolder.listFiles((p) -> p.isDirectory() && "playerdata".equals(p.getName()));
			if (playerDataFolder != null && playerDataFolder.length == 1)
				playerFiles = playerDataFolder[0]
						.listFiles((p) -> p.isFile() && p.getName().equals(uuid.toString() + ".dat"));
			if (playerFiles != null && playerFiles.length == 1)
				return NBTUtil.read(playerFiles[0]);
		}

		return null;
	}
}
