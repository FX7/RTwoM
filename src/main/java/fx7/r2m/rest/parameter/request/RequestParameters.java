package fx7.r2m.rest.parameter.request;

import java.util.Set;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.ParametersProvider;

public abstract class RequestParameters implements ParametersProvider
{
	protected final Set<EntityAccess> entityAccess;

	RequestParameters(Set<EntityAccess> entityAccess)
	{
		this.entityAccess = entityAccess;
	}

	protected double asDouble(String parameter) throws RestException
	{
		try
		{
			return Double.parseDouble(parameter);
		} catch (NumberFormatException e)
		{
			throw RestException.invalidParameter("'" + parameter + "' is not a valid double parameter");
		}
	}

	protected int asInteger(String parameter) throws RestException
	{
		try
		{
			return Integer.parseInt(parameter);
		} catch (NumberFormatException e)
		{
			throw RestException.invalidParameter("'" + parameter + "' is not a valid integer parameter");
		}
	}
}
