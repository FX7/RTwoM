package fx7.r2m.rest.handler;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.handler.player.GetLastLocationAction;
import fx7.r2m.rest.handler.player.GetLastPlayedAction;
import fx7.r2m.rest.handler.script.ExecuteAction;
import fx7.r2m.rest.handler.world.AddItemStackToContainerAction;
import fx7.r2m.rest.handler.world.GetInventoryFromContainerAction;
import fx7.r2m.rest.handler.world.GetMaterialAction;
import fx7.r2m.rest.handler.world.RemoveItemStackFromContainerAction;
import fx7.r2m.rest.handler.world.SetMaterialAction;

public class RestActionFactory
{
	public static RestAction getRestAction(Context context, String entityAction) throws RestException
	{
		switch (context)
		{
		case WORLD:
			return getWorldAction(entityAction);
		case PLAYER:
			return getPlayerAction(entityAction);
		case SCRIPT:
			return getScriptAction(entityAction);
		default:
			throw RestException.unknownAction(entityAction, context);
		}
	}

	private static RestAction getPlayerAction(String action) throws RestException
	{
		if ("getLastPlayed".equals(action))
			return new GetLastPlayedAction(action);
		else if ("getLastLocation".equals(action))
			return new GetLastLocationAction(action);
//		else if ("getLastDirection".equals(action))
//			return new GetLastDirectionAction(action);
		throw RestException.unknownAction(action, Context.PLAYER);
	}

	private static RestAction getWorldAction(String action) throws RestException
	{
		if ("getMaterial".equals(action))
			return new GetMaterialAction(action);
		else if ("setMaterial".equals(action))
			return new SetMaterialAction(action);
		else if ("addToContainer".equals(action))
			return new AddItemStackToContainerAction(action);
		else if ("removeFromContainer".equals(action))
			return new RemoveItemStackFromContainerAction(action);
		else if ("getInventory".equals(action))
			return new GetInventoryFromContainerAction(action);
		throw RestException.unknownAction(action, Context.WORLD);
	}

	private static RestAction getScriptAction(String action) throws RestException
	{
		if ("execute".equals(action))
			return new ExecuteAction(action);
		throw RestException.unknownAction(action, Context.SCRIPT);
	}
}
