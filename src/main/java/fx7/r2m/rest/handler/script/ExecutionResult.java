package fx7.r2m.rest.handler.script;

import java.util.HashMap;
import java.util.Map;

import fx7.r2m.rest.RestJsonReturnable;

public class ExecutionResult implements RestJsonReturnable
{
	private Map<String, RestJsonReturnable> results = new HashMap<>();

	public ExecutionResult(Map<String, RestJsonReturnable> results)
	{
		this.results = results;
	}

	@Override
	public int getHttpStatusCode()
	{
		return HTTP_OK;
	}

	@Override
	public String toString()
	{
		return "ExecutionResult [results=" + results + "]";
	}
}
