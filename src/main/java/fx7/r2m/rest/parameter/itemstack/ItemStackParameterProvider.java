package fx7.r2m.rest.parameter.itemstack;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;

public interface ItemStackParameterProvider
{
	public boolean hasMoreItemStack();

	public ItemStack peekItemStack() throws RestException;

	public ItemStack consumeItemStack() throws RestException;

	public static ItemStack fromParameters(Set<EntityAccess> entityAccess, String materialName, int amount) throws RestException
	{
		Material material = MaterialParameterProvider.fromParameters(entityAccess, materialName);
		if (!material.isItem())
			throw RestException.invalidParameter("'" + materialName + "' is not a valid item");

		return new ItemStack(material, amount);
	}
}
