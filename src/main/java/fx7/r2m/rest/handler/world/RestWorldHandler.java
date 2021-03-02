package fx7.r2m.rest.handler.world;

import org.bukkit.World;

import fx7.r2m.Coordinator;
import fx7.r2m.access.Context;
import fx7.r2m.rest.handler.AbstractEntityRestHandler;

public class RestWorldHandler extends AbstractEntityRestHandler<World>
{
	public static final Context CONTEXT = Context.WORLD;

	public RestWorldHandler(Coordinator coordinator)
	{
		super(coordinator);
	}

	@Override
	protected Context getContext()
	{
		return CONTEXT;
	}
}
