package fx7.r2m.entity;

import fx7.r2m.rest.RestJsonReturnable;

public interface Entity extends RestJsonReturnable
{
	@Override
	public default int getHttpStatusCode()
	{
		return HTTP_OK;
	}
}
