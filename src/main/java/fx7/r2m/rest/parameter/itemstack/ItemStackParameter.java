package fx7.r2m.rest.parameter.itemstack;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;
import fx7.r2m.rest.parameter.RestParameter;

public class ItemStackParameter implements RestParameter, MinecraftParameter<ItemStack>
{
	private int amount;
	private String material;

	public ItemStackParameter(int amount, Material material)
	{
		this(amount, material.name());
	}

	public ItemStackParameter(int amount, String material)
	{
		this.amount = amount;
		this.material = material;
	}

	@Override
	public ItemStack toMinecraftParameter(Set<EntityAccess> entityAccess) throws RestException
	{
		return ItemStackParameterProvider.fromParameters(entityAccess, material, amount);
	}

	public static ItemStackParameter fromItemStack(ItemStack itemStack)
	{
		if (itemStack == null)
			return null;
		return new ItemStackParameter(itemStack.getAmount(), itemStack.getType());
	}
}
