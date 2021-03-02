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

public interface ParametersProvider extends ItemStackParameterProvider, LocationParameterProvider,
		MaterialParameterProvider, PlayerParameterProvider
{
	public static void setParameters(RestAction action, ParametersProvider provider)
	{
		if (action instanceof ItemStackParameterReceiver)
			((ItemStackParameterReceiver) action).setItemStackParameter(provider);
		if (action instanceof LocationParameterReceiver)
			((LocationParameterReceiver) action).setLocationParameter(provider);
		if (action instanceof MaterialParameterReceiver)
			((MaterialParameterReceiver) action).setMaterialParameter(provider);
		if (action instanceof PlayerParameterReceiver)
			((PlayerParameterReceiver) action).setPlayerParameter(provider);
	}
}
