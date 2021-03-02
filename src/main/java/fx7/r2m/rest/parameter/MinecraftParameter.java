package fx7.r2m.rest.parameter;

import fx7.r2m.rest.RestException;

public interface MinecraftParameter<T>
{
	public T toMinecraftParameter() throws RestException;
}
