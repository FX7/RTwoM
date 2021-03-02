package fx7.r2m.rest.parameter;

import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterReceiver;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterReceiver;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterReceiver;

public interface ParametersReceiver extends ItemStackParameterReceiver, LocationParameterReceiver,
		MaterialParameterReceiver, PlayerParameterReceiver
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
	}
}
