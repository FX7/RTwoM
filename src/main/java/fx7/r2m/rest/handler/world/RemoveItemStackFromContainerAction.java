package fx7.r2m.rest.handler.world;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterReceiver;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class RemoveItemStackFromContainerAction extends RestAction
		implements LocationParameterReceiver, ItemStackParameterReceiver
{
	private LocationParameterProvider locationParameter;
	private ItemStackParameterProvider itemStackParameter;

	public RemoveItemStackFromContainerAction(String actionName)
	{
		super(Context.WORLD, RequestMethod.POST, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		if (itemStackParameter == null)
			throw RestException.invalidParameter("ItemStackParameter missing.");

		Location location = locationParameter.peekLocation();
		ItemStack itemStack = itemStackParameter.peekItemStack();

		// TODO handle unremoved Items
		Map<Integer, ItemStack> unRemovedItems = coordinator
				.callSyncMethod(() -> removeItemStackFromContainer(location, itemStack));
		return RestReturnable.simpleOK();
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameter = parameter;
	}

	@Override
	public void setItemStackParameter(ItemStackParameterProvider parameter)
	{
		this.itemStackParameter = parameter;
	}

	private Map<Integer, ItemStack> removeItemStackFromContainer(Location location, ItemStack itemStack)
			throws RestException
	{
		Map<Integer, ItemStack> unremovedItems = new HashMap<>();
		Block block = location.getBlock();
		BlockState state = block.getState();
		if (state instanceof Container)
		{
			Container chest = (Container) state;
			unremovedItems = chest.getSnapshotInventory().removeItem(itemStack);
			boolean update = chest.update(true);
			if (!update)
				throw RestException.blockUpdateFailed(location);
		} else
			throw RestException.invalidParameter("Block at given location is no Container!");

		return unremovedItems;
	}
}
