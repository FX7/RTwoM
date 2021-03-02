package fx7.r2m.rest;

import org.bukkit.Location;

import fx7.r2m.access.Context;
import fx7.r2m.rest.handler.AbstractEntityRestHandler;

public class RestException extends Exception implements RestReturnable
{
	private static final long serialVersionUID = 1L;

	private final String message;
	private final int httpStatusCode;
	private final boolean forceLogToConsole;

	private RestException(String message, int httpStatusCode)
	{
		this(message, httpStatusCode, false);
	}

	private RestException(String message, int httpStatusCode, boolean forceLogToConsole)
	{
		this.message = message;
		this.httpStatusCode = httpStatusCode;
		this.forceLogToConsole = forceLogToConsole;
	}

	@Override
	public byte[] getHttpResponseMessage()
	{
		return message.getBytes();
	}

	@Override
	public int getHttpStatusCode()
	{
		return httpStatusCode;
	}

	@Override
	public String getHttpContentType()
	{
		return RestReturnable.CONTENT_TYPE_TEXTPLAIN;
	}

	@Override
	public boolean serverError()
	{
		if (forceLogToConsole)
			return true;
		return RestReturnable.super.serverError();
	}

	public static RestException fromAnyRestReturnable(RestReturnable restReturnable)
	{
		if (restReturnable instanceof RestException)
			return (RestException) restReturnable;
		RestException exception = new RestException("", restReturnable.getHttpStatusCode())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public byte[] getHttpResponseMessage()
			{
				return restReturnable.getHttpResponseMessage();
			}

			@Override
			public String getHttpContentType()
			{
				return restReturnable.getHttpContentType();
			}
		};

		return exception;
	}

	public static RestException blockUpdateFailed(Location location)
	{
		String message = "Update of block at '" + location.getWorld() + ":" + location.getX() + ":" + location.getY()
				+ ":" + location.getY() + "' failed.";
		return new RestException(message, HTTP_BAD_REQUEST);
	}

	public static RestException unexpectedException(Exception exeption)
	{
		return new RestException(exeption.getMessage(), HTTP_INTERNAL_ERROR, true);
	}

	public static RestException unexpectedBehavior(String message)
	{
		return new RestException(message, HTTP_BAD_REQUEST, true);
	}

	public static RestException unknownAction(String action, Context context)
	{
		String message = "Unknown action '" + action + "' for context '" + context + "'.";
		return new RestException(message, HTTP_BAD_REQUEST);
	}

	public static RestException unknownMethod(String method, Context context)
	{
		String message = "Unknown method '" + method + "' for context '" + context + "'.";
		return new RestException(message, HTTP_BAD_REQUEST);
	}

	public static RestException invalidMethod(String method, Context context, String action)
	{
		String message = "Invalid method '" + method + "' for context '" + context + "' and action '" + action + "'.";
		return new RestException(message, HTTP_BAD_REQUEST);
	}

	public static RestException noContextAccess(Context context)
	{
		String message = "No access to context. '" + context + "'.";
		return new RestException(message, HTTP_FORBIDDEN);
	}

	public static RestException noAppAccess()
	{
		String message = "Invalid header " + AbstractEntityRestHandler.HEAD_APP_NAME + "/"
				+ AbstractEntityRestHandler.HEAD_APP_KEY + ".";
		return new RestException(message, HTTP_FORBIDDEN);
	}

	public static RestException noEntityAccess(String entityId, Context context)
	{
		String message = "No access to entity '" + entityId + "' in context " + context + "'.";
		return new RestException(message, HTTP_FORBIDDEN);
	}

	public static RestException invalidParameter(String message)
	{
		return new RestException(message, HTTP_BAD_REQUEST);
	}
}
