package fx7.r2m.rest.handler.script;

import fx7.r2m.Coordinator;
import fx7.r2m.access.Context;
import fx7.r2m.entity.script.ScriptEntity;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestHttpExchange;
import fx7.r2m.rest.handler.AbstractEntityRestHandler;

public class RestScriptHandler extends AbstractEntityRestHandler<ScriptEntity>
{
	public static final Context CONTEXT = Context.SCRIPT;

	public RestScriptHandler(Coordinator coordinator)
	{
		super(coordinator);
	}

	@Override
	protected Context getContext()
	{
		return CONTEXT;
	}

	@Override
	protected RestAction getAction(RestHttpExchange exchange) throws RestException
	{
		RestAction action = super.getAction(exchange);
		if (action instanceof ExecuteAction)
		{
			ExecuteAction ea = (ExecuteAction) action;
			ScriptEntity script = coordinator.getScriptManager().getScript(exchange.getEntityId());
			ea.init(script, exchange.getEntityAccess());
		}
		return action;
	}
}
