package fx7.r2m.rest.handler;

import java.io.IOException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fx7.r2m.Coordinator;
import fx7.r2m.access.AppAccess;
import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestHttpExchange;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.ParametersProvider;

public abstract class AbstractEntityRestHandler<En> implements HttpHandler
{
	public static final String HEAD_APP_NAME = "App-Name";
	public static final String HEAD_APP_KEY = "App-Key";
	public static final String HEAD_ENTITY_KEY = "Entity-Key";

	protected final Coordinator coordinator;

	protected AbstractEntityRestHandler(Coordinator coordinator)
	{
		this.coordinator = coordinator;
		coordinator.sendConsoleMessage(
				this.getClass().getSimpleName() + " listening at context '" + getContext().asContext() + "'");
	}

	@Override
	public final void handle(HttpExchange httpExchange) throws IOException
	{
		try
		{
			RestHttpExchange restExchange = checkAccess(httpExchange);
			RestAction action = getAction(restExchange);
			action.setCoordinator(coordinator);
			if (action.getValidMethod() != restExchange.getRequestMethod())
				throw RestException.invalidMethod(httpExchange.getRequestMethod(), getContext(),
						restExchange.getEntityAction());
			RestReturnable result = action.excecute();
			result.writeResponse(coordinator, httpExchange);
		} catch (RestException re)
		{
			re.writeResponse(coordinator, httpExchange);
		} catch (Throwable t)
		{
			coordinator.sendConsoleMessage(new String(t.getMessage()));
			httpExchange.sendResponseHeaders(RestReturnable.HTTP_INTERNAL_ERROR, -1);
		}
	}

	private RestHttpExchange checkAccess(HttpExchange httpExchange) throws RestException
	{
		Headers headers = httpExchange.getRequestHeaders();

		String appName = headers.getFirst(HEAD_APP_NAME);
		String appKey = headers.getFirst(HEAD_APP_KEY);
		String entityKey = headers.getFirst(HEAD_ENTITY_KEY);

		AppAccess appAccess = coordinator.getAccessManager().getAccess(appName, appKey);

		RestHttpExchange restExchange = new RestHttpExchange(httpExchange, appAccess, coordinator.getScriptManager(),
				getContext());
		appAccess.checkAccess(getContext(), restExchange.getEntityId(), entityKey);

		coordinator.sendConsoleMessage(
				"Access to '" + httpExchange.getRequestURI().getPath() + " for App '" + appName + "' granted.");
		return restExchange;
	}

	protected abstract Context getContext();

	protected RestAction getAction(RestHttpExchange exchange) throws RestException
	{
		RestAction action = RestActionFactory.createRestAction(getContext(), exchange.getEntityAction());
		ParametersProvider.setParameters(action, exchange);
		return action;
	}
}
