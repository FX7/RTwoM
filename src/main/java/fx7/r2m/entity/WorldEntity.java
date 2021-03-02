package fx7.r2m.entity;

import org.bukkit.Bukkit;
import org.bukkit.World;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;

public class WorldEntity extends ContextEntity implements MinecraftParameter<World>
{
	public WorldEntity(String name)
	{
		super(Context.WORLD, name);
	}

	@Override
	public World toMinecraftParameter() throws RestException
	{
		World world = getWorld(getEntityName());
		if (world == null)
			throw RestException.invalidParameter("'" + getEntityName() + "' is not a valid Worldname");
		return world;
	}

	public static World getWorld(String name)
	{
		if (name == null)
			return null;

		return Bukkit.getWorld(name);
	}
}
