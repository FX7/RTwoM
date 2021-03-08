package fx7.r2m.rest.parameter;

import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.parameter.inventory.InventoryParameterProvider;
import fx7.r2m.rest.parameter.inventory.InventoryParameterReceiver;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterReceiver;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterReceiver;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterReceiver;
import fx7.r2m.rest.parameter.powerable.PowerParameterProvider;
import fx7.r2m.rest.parameter.powerable.PowerParameterReceiver;

public interface ParametersReceiver extends ItemStackParameterReceiver, LocationParameterReceiver,
		MaterialParameterReceiver, PlayerParameterReceiver, InventoryParameterReceiver, PowerParameterReceiver
{
	public static void extractParameters(RestAction action, ParametersReceiver receiver)
	{
		if (action instanceof ItemStackParameterProvider)
			receiver.setItemStackParameter((ItemStackParameterProvider) action);
		if (action instanceof LocationParameterProvider)
			receiver.setLocationParameter((LocationParameterProvider) action);
		if (action instanceof MaterialParameterProvider)
			receiver.setMaterialParameter((MaterialParameterProvider) action);
		if (action instanceof PlayerParameterProvider)
			receiver.setPlayerParameter((PlayerParameterProvider) action);
		if (action instanceof InventoryParameterProvider)
			receiver.setInventoryParameter((InventoryParameterProvider) action);
		if (action instanceof PowerParameterProvider)
			receiver.setPowerParameter((PowerParameterProvider) action);
	}
}
