package fx7.r2m.rest.handler.script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
import fx7.r2m.rest.parameter.inventory.InventoryParameterProvider;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.server.RequestMethod;

public class ExecuteAction extends RestAction implements ParametersProvider, ParametersReceiver
{
	private ScriptEntity script;

	private ParameterList<ItemStack> itemStackParameters = new ParameterList<>();

	private ParameterList<Inventory> inventoryParameters = new ParameterList<>();

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
		Map<String, RestJsonReturnable> resultMap = new LinkedHashMap<>();
		for (RestAction ra : script.getRestActions())
		{
			ParametersProvider.setParameters(ra, this);
			ra.init(coordinator, entityAccess);
			RestReturnable result = ra.excecute();
			if (!result.success())
				throw RestException.fromAnyRestReturnable(result);
			resultMap.put(ra.getActionName(), RestJsonReturnable.fromAnyRestReturnable(result));
			// shifting parameters
			ParametersReceiver.extractParameters(ra, this);
		}

		return new ExecutionResult(resultMap); // list -> single action
	}

	public void init(ScriptEntity script, Set<EntityAccess> entityAccess)
	{
		this.script = script;
		this.entityAccess = entityAccess;
	}

	@Override
	public Set<EntityAccess> getEntityAccess()
	{
		return entityAccess;
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
	public void setInventoryParameter(InventoryParameterProvider parameter)
	{
		this.inventoryParameters.addParameters(new ParameterProvider<Inventory>()
		{

			@Override
			public boolean hasMore()
			{
				return parameter.hasMoreInventory();
			}

			@Override
			public Inventory peek() throws RestException
			{
				return parameter.peekInventory();
			}

			@Override
			public Inventory consume() throws RestException
			{
				return parameter.consumeInventory();
			}
		});
	}

	@Override
	public boolean hasMoreInventory()
	{
		return inventoryParameters.hasMore();
	}

	@Override
	public Inventory peekInventory() throws RestException
	{
		return inventoryParameters.peek();
	}

	@Override
	public Inventory consumeInventory() throws RestException
	{
		return inventoryParameters.consume();
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
		private List<ParameterProvider<T>> providers = new ArrayList<>();

		public void addParameters(ParameterProvider<T> provider)
		{
			providers.add(provider);
		}

		public boolean hasMore()
		{
			return providers.stream().anyMatch(p -> p.hasMore());
		}

		public T peek() throws RestException
		{
			return getNext(false);
		}

		public T consume() throws RestException
		{
			return getNext(true);
		}

		private T getNext(boolean consume) throws RestException
		{
			ParameterProvider<T> next = null;
			for (int i = 0; i < providers.size() && next == null; i++)
			{
				ParameterProvider<T> provider = providers.get(i);
				if (provider.hasMore())
					next = provider;
			}

			if (next == null)
				throw RestException.invalidParameter("No (more) Parameters");

			if (consume)
				return next.consume();
			return next.peek();
		}
	}
}
