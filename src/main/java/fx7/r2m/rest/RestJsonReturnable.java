package fx7.r2m.rest;

import com.google.gson.Gson;

public interface RestJsonReturnable extends RestReturnable
{
	@Override
	public default byte[] getHttpResponseMessage()
	{
		Gson gson = new Gson();
		return gson.toJson(this).getBytes();
	}

	@Override
	public default String getHttpContentType()
	{
		return CONTENT_TYPE_JSON;
	}

	public static RestJsonReturnable fromAnyRestReturnable(RestReturnable returnable)
	{
		if (returnable instanceof RestJsonReturnable)
			return (RestJsonReturnable) returnable;

		RestJsonReturnable result = new RestJsonReturnable()
		{
			@Override
			public byte[] getHttpResponseMessage()
			{
				String message = "";
				if (returnable.getHttpResponseMessage() != null)
					message = new String(returnable.getHttpResponseMessage());
				message = "{'message':'" + message + "','httpStatusCode':'" + getHttpStatusCode() + "'}";
				return message.getBytes();
			}

			@Override
			public int getHttpStatusCode()
			{
				return returnable.getHttpStatusCode();
			}
		};
		return result;
	}
}
