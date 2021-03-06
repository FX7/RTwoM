package fx7.r2m.entity;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;

import fx7.r2m.access.AccessManager;
import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;

public class WorldEntity extends ContextEntity implements MinecraftParameter<World>
{
	public WorldEntity(String name)
	{
		super(Context.WORLD, name);
	}

	@Override
	public World toMinecraftParameter(Set<EntityAccess> entityAccess) throws RestException
	{
		World world = getWorld(entityAccess, getEntityName());
		if (world == null)
			throw RestException.invalidParameter("'" + getEntityName() + "' is not a valid Worldname");
		return world;
	}

	public static World getWorld(Set<EntityAccess> entityAccess, String name) throws RestException
	{
		if (!AccessManager.getInstance().hasAccess(entityAccess, Context.WORLD, name))
			throw RestException.noEntityAccess(name, Context.WORLD);

		if (name == null)
			return null;

		return Bukkit.getWorld(name);
	}
}
