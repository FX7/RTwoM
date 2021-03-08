package fx7.r2m.rest.parameter.powerable;

import fx7.r2m.rest.RestException;

public interface PowerParameterProvider
{
	public boolean hasMorePower();

	public boolean peekPower() throws RestException;

	public boolean consumePower() throws RestException;
}
