package fx7.r2m.rest.handler.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import fx7.r2m.access.AppAccess;
import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.entity.script.ScriptEntity;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestJsonReturnable;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.ParameterProvider;
import fx7.r2m.rest.parameter.ParametersProvider;
import fx7.r2m.rest.parameter.ParametersReceiver;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class ExecuteAction extends RestAction implements ParametersProvider, ParametersReceiver
{
	private List<EntityAccess> access;
	private AppAccess appAccess;

	private ScriptEntity script;

	private ParameterList<ItemStack> itemStackParameters = new ParameterList<>();

	private ParameterList<Location> locationParameters = new ParameterList<>();

	private ParameterList<Material> materialParameters = new ParameterList<>();

	private ParameterList<OfflinePlayer> playerParameters = new ParameterList<>();

	public ExecuteAction(String actionName)
	{
		super(Context.SCRIPT, RequestMethod.POST, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		Map<String, RestJsonReturnable> resultMap = new HashMap<>();
		for (RestAction ra : script.getRestActions())
		{
			checkAccess(ra);
			ParametersProvider.setParameters(ra, this);
			// check access
			ra.setCoordinator(coordinator);
			RestReturnable result = ra.excecute();
			if (!result.success())
				throw RestException.fromAnyRestReturnable(result);
			resultMap.put(ra.getActionName(), RestJsonReturnable.fromAnyRestReturnable(result));
			// shifting parameters
			ParametersReceiver.extractParameters(ra, this);
		}

		return new ExecutionResult(resultMap); // list -> single action
	}

	private void checkAccess(RestAction action) throws RestException
	{
		if (action instanceof LocationParameterReceiver)
		{
			Context context = Context.WORLD;
			Location location = peekLocation();
			String entityName = location.getWorld().getName();

			EntityAccess access = getEntityAccess(context, entityName);
			appAccess.checkAccess(access);
		}
		if (action instanceof PlayerParameterReceiver)
		{
			Context context = Context.PLAYER;
			OfflinePlayer player = peekPlayer();
			String entityName = player.getName();

			EntityAccess access = getEntityAccess(context, entityName);
			appAccess.checkAccess(access);
		}
	}

	private EntityAccess getEntityAccess(Context context, String entityName) throws RestException
	{
		Optional<EntityAccess> entityAccess = access.stream()
				.filter(a -> a.getContext() == context && a.getEntityName().equals(entityName)).findFirst();
		if (!entityAccess.isPresent())
			throw RestException.noEntityAccess(entityName, context);

		return entityAccess.get();
	}

	public void init(ScriptEntity script, AppAccess appAccess, List<EntityAccess> access)
	{
		this.script = script;
		this.appAccess = appAccess;
		this.access = access;
	}

	@Override
	public void setItemStackParameter(ItemStackParameterProvider parameter)
	{
		this.itemStackParameters.addParameters(new ParameterProvider<ItemStack>()
		{

			@Override
			public boolean hasMore()
			{
				return parameter.hasMoreItemStack();
			}

			@Override
			public ItemStack peek() throws RestException
			{
				return parameter.peekItemStack();
			}

			@Override
			public ItemStack consume() throws RestException
			{
				return parameter.consumeItemStack();
			}
		});
	}

	@Override
	public boolean hasMoreItemStack()
	{
		return this.itemStackParameters.hasMore();
	}

	@Override
	public ItemStack peekItemStack() throws RestException
	{
		return this.itemStackParameters.peek();
	}

	@Override
	public ItemStack consumeItemStack() throws RestException
	{
		return this.itemStackParameters.consume();
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameters.addParameters(new ParameterProvider<Location>()
		{

			@Override
			public boolean hasMore()
			{
				return parameter.hasMoreLocation();
			}

			@Override
			public Location peek() throws RestException
			{
				return parameter.peekLocation();
			}

			@Override
			public Location consume() throws RestException
			{
				return parameter.consumeLocation();
			}
		});
	}

	@Override
	public boolean hasMoreLocation()
	{
		return locationParameters.hasMore();
	}

	@Override
	public Location peekLocation() throws RestException
	{
		return locationParameters.peek();
	}

	@Override
	public Location consumeLocation() throws RestException
	{
		return locationParameters.consume();
	}

	@Override
	public void setMaterialParameter(MaterialParameterProvider parameter)
	{
		this.materialParameters.addParameters(new ParameterProvider<Material>()
		{

			@Override
			public boolean hasMore()
			{
				return parameter.hasMoreMaterial();
			}

			@Override
			public Material peek() throws RestException
			{
				return parameter.peekMaterial();
			}

			@Override
			public Material consume() throws RestException
			{
				return parameter.consumeMaterial();
			}
		});
	}

	@Override
	public boolean hasMoreMaterial()
	{
		return materialParameters.hasMore();
	}

	@Override
	public Material peekMaterial() throws RestException
	{
		return materialParameters.peek();
	}

	@Override
	public Material consumeMaterial() throws RestException
	{
		return materialParameters.consume();
	}

	@Override
	public void setPlayerParameter(PlayerParameterProvider parameter)
	{
		this.playerParameters.addParameters(new ParameterProvider<OfflinePlayer>()
		{

			@Override
			public boolean hasMore()
			{
				return parameter.hasMorePlayer();
			}

			@Override
			public OfflinePlayer peek() throws RestException
			{
				return parameter.peekPlayer();
			}

			@Override
			public OfflinePlayer consume() throws RestException
			{
				return parameter.consumePlayer();
			}
		});
	}

	@Override
	public boolean hasMorePlayer()
	{
		return playerParameters.hasMore();
	}

	@Override
	public OfflinePlayer peekPlayer() throws RestException
	{
		return playerParameters.peek();
	}

	@Override
	public OfflinePlayer consumePlayer() throws RestException
	{
		return playerParameters.consume();
	}

	private static class ParameterList<T>
	{
		private int index = -1;
		private List<T> parameters = new ArrayList<>();

		public void addParameters(ParameterProvider<T> provider)
		{
			while (provider.hasMore())
			{
				try
				{
					this.parameters.add(provider.consume());
				} catch (RestException e)
				{
					// Exception Handling
				}
			}
			index = this.parameters.size() - 1;
		}

		public boolean hasMore()
		{
			return index >= 0;
		}

		public T peek() throws RestException
		{
			return getNext(false);
		}

		public T consume() throws RestException
		{
			return getNext(true);
		}

		private T getNext(boolean shift) throws RestException
		{
			if (!hasMore())
				throw RestException.invalidParameter("No (more) Parameters");

			T result = parameters.get(index);

			if (shift)
			{
				index--;
				if (index < 0)
					index = parameters.size() - 1;
			}
			return result;
		}
	}
}
