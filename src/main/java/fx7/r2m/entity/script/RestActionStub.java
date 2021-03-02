package fx7.r2m.entity.script;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.handler.RestActionFactory;

@SerializableAs("RestActionStub")
public class RestActionStub implements ConfigurationSerializable
{
	private static final String PARAM_CONTEXT = "context";
	private Context context;

	private static final String PARAM_ACTION = "action";
	private String action;

	public RestActionStub(Context context, String action)
	{
		this.context = context;
		this.action = action;
	}

	public RestAction toRestAction() throws RestException
	{
		return RestActionFactory.createRestAction(context, action);
	}

	public static RestActionStub deserialize(Map<String, Object> serialized)
	{
		return new RestActionStub(//
				Context.fromString((String) serialized.get(PARAM_CONTEXT)), //
				(String) serialized.get(PARAM_ACTION));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> serialized = new HashMap<>();

		serialized.put(PARAM_CONTEXT, context.name());
		serialized.put(PARAM_ACTION, action);

		return serialized;
	}
}
