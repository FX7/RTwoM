package fx7.r2m.rest.handler.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.inventory.InventoryParameter;
import fx7.r2m.rest.parameter.inventory.InventoryParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class GetInventoryFromContainerAction extends RestAction
		implements LocationParameterReceiver, InventoryParameterProvider
{
	private LocationParameterProvider locationParameter;

	private Inventory inventory;

	public GetInventoryFromContainerAction(String actionName)
	{
		super(Context.WORLD, RequestMethod.GET, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		return InventoryParameter.fromInventory(peekInventory());
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameter = parameter;
	}

	private Inventory getInventoryFromContainer(Location location) throws RestException
	{
		Block block = location.getBlock();
		BlockState state = block.getState();
		if (state instanceof Container)
		{
			Container chest = (Container) state;
			Inventory inventory = chest.getSnapshotInventory();
			return inventory;
		} else
			throw RestException.invalidParameter("Block at given location is no Container!");
	}

	@Override
	public boolean hasMoreInventory()
	{
		try
		{
			return peekInventory() != null;
		} catch (RestException e)
		{
			return false;
		}
	}

	@Override
	public Inventory peekInventory() throws RestException
	{
		return getNextInventory(false);
	}

	@Override
	public Inventory consumeInventory() throws RestException
	{
		return getNextInventory(true);
	}

	public Inventory getNextInventory(boolean consume) throws RestException
	{
		if (inventory != null)
		{
			Inventory inventory = this.inventory;
			if (consume)
			{
				this.locationParameter = null;
				this.inventory = null;
			}
			return inventory;
		}

		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		Location location = locationParameter.peekLocation();

		Inventory inventory = coordinator.callSyncMethod(() -> getInventoryFromContainer(location));
		this.inventory = inventory;
		return inventory;
	}
}
