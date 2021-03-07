package fx7.r2m.rest.parameter.inventory;

import org.bukkit.inventory.Inventory;

import fx7.r2m.rest.RestException;

public interface InventoryParameterProvider
{
	public boolean hasMoreInventory();

	public Inventory peekInventory() throws RestException;

	public Inventory consumeInventory() throws RestException;
}
