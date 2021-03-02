package fx7.r2m.rest.parameter;

import fx7.r2m.rest.RestJsonReturnable;

public interface RestParameter extends RestJsonReturnable
{
	@Override
	default int getHttpStatusCode()
	{
		return HTTP_OK;
	}

}
