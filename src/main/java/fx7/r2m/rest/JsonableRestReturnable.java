package fx7.r2m.rest;

public class JsonableRestReturnable implements RestJsonReturnable
{
	private final int httpStatusCode;
	private final String contentType;
	private final String content;

	public JsonableRestReturnable(RestReturnable returnable)
	{
		this(//
				returnable.getHttpStatusCode(), //
				returnable.getHttpContentType(), //
				returnable.getHttpResponseMessage() != null ? new String(returnable.getHttpResponseMessage()) : null);
	}

	private JsonableRestReturnable(int httpStatusCode, String contentType, String content)
	{
		this.httpStatusCode = httpStatusCode;
		this.contentType = contentType;
		this.content = content;
	}

	@Override
	public int getHttpStatusCode()
	{
		return httpStatusCode;
	}
}
