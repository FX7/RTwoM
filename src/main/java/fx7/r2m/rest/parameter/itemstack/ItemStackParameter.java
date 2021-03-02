package fx7.r2m.rest.parameter.itemstack;

import org.bukkit.inventory.ItemStack;

import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;
import fx7.r2m.rest.parameter.RestParameter;

public class ItemStackParameter implements RestParameter, MinecraftParameter<ItemStack>
{
	public static final String POST_PARAMETER_KEY = "itemStack";

	private int amount;
	private String material;

	@Override
	public ItemStack toMinecraftParameter() throws RestException
	{
		return ItemStackParameterProvider.fromParameters(material, amount);
	}
}
