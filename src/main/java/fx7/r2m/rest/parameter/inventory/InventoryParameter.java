package fx7.r2m.rest.parameter.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fx7.r2m.rest.parameter.RestParameter;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameter;

public class InventoryParameter implements RestParameter
{
	private final List<ItemStackParameter> itemStacks = new ArrayList<>();

	public InventoryParameter()
	{
	}

	public void addItemStack(ItemStack itemStack)
	{
		ItemStackParameter itemStackParameter = ItemStackParameter.fromItemStack(itemStack);
		if (itemStackParameter != null)
			this.itemStacks.add(itemStackParameter);
	}

	public static InventoryParameter fromInventory(Inventory inventory)
	{
		InventoryParameter parameter = new InventoryParameter();

		for (ItemStack stack : inventory.getStorageContents())
		{
			parameter.addItemStack(stack);
		}

		return parameter;
	}
}
