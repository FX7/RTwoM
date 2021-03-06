package fx7.r2m.rest.parameter;

import java.util.Set;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;

public interface MinecraftParameter<T>
{
	public T toMinecraftParameter(Set<EntityAccess> entityAccess) throws RestException;
}
