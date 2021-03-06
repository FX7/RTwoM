package fx7.r2m.rest.parameter.location;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.entity.WorldEntity;
import fx7.r2m.rest.RestException;

public interface LocationParameterProvider
{
	public boolean hasMoreLocation();

	public Location peekLocation() throws RestException;

	public Location consumeLocation() throws RestException;

	public static Location fromParameters(Set<EntityAccess> entityAccess, String worldName, double x, double y,
			double z) throws RestException
	{
		World world = WorldEntity.getWorld(entityAccess, worldName);
		if (world == null)
			throw RestException.invalidParameter("'" + worldName + "' is not a valid valid world");

		return new Location(world, x, y, z);
	}
}
