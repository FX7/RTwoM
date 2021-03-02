package fx7.r2m.rest;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import fx7.r2m.Coordinator;

public interface RestReturnable
{
	public static final int HTTP_OK = 200;
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_FORBIDDEN = 403;

	public static final int HTTP_INTERNAL_ERROR = 500;

	public static final String CONTENT_TYPE_TEXTPLAIN = "text/plain";
	public static final String CONTENT_TYPE_JSON = "application/json";

	public byte[] getHttpResponseMessage();

	public int getHttpStatusCode();

	public String getHttpContentType();

	public default boolean success()
	{
		return getHttpStatusCode() >= 200 && getHttpStatusCode() <= 299;
	}

	public default boolean clientError()
	{
		return getHttpStatusCode() >= 400 && getHttpStatusCode() <= 499;
	}

	public default boolean serverError()
	{
		return getHttpStatusCode() >= 500 && getHttpStatusCode() <= 599;
	}

	public default void writeResponse(Coordinator coordinator, HttpExchange httpExchange) throws IOException
	{
		byte[] message = getHttpResponseMessage();
		int messageLength = message != null ? message.length : -1;
		if (messageLength > -1 && serverError())
			coordinator.sendConsoleMessage(new String(message));
		httpExchange.sendResponseHeaders(getHttpStatusCode(), messageLength);
		if (messageLength > -1)
		{
			httpExchange.getResponseHeaders().add("Content-Type", getHttpContentType());
			OutputStream outputStream = httpExchange.getResponseBody();
			outputStream.write(message);
			outputStream.flush();
			outputStream.close();
		}
	}

	public static RestReturnable plainTextOK(String message)
	{
		return new RestReturnable()
		{
			@Override
			public byte[] getHttpResponseMessage()
			{
				return message != null ? message.getBytes() : null;
			}

			@Override
			public int getHttpStatusCode()
			{
				return HTTP_OK;
			}

			@Override
			public String getHttpContentType()
			{
				return CONTENT_TYPE_TEXTPLAIN;
			}
		};
	}

	public static RestReturnable simpleOK()
	{
		return simpleCode(HTTP_OK);
	}

	public static RestReturnable simpleCode(int code)
	{
		return new RestReturnable()
		{
			@Override
			public byte[] getHttpResponseMessage()
			{
				return null;
			}

			@Override
			public int getHttpStatusCode()
			{
				return code;
			}

			@Override
			public String getHttpContentType()
			{
				return null;
			}
		};
	}
}
