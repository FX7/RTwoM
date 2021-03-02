package fx7.r2m.rest.parameter.request;

import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.ParametersProvider;

public abstract class RequestParameters implements ParametersProvider
{
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
