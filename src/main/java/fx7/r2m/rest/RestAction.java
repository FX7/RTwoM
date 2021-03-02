package fx7.r2m.rest;

import fx7.r2m.Coordinator;
import fx7.r2m.access.Context;
import fx7.r2m.rest.server.RequestMethod;

public abstract class RestAction
{
	private final Context context;
	private final RequestMethod method;
	private final String actionName;

	protected Coordinator coordinator;

	protected RestAction(Context context, RequestMethod method, String actionName)
	{
		this.context = context;
		this.method = method;
		this.actionName = actionName;
	}

	public void setCoordinator(Coordinator coordinator)
	{
		this.coordinator = coordinator;
	}

	public Context getContext()
	{
		return context;
	}

	public RequestMethod getValidMethod()
	{
		return method;
	}

	public String getActionName()
	{
		return actionName;
	}

	public abstract RestReturnable excecute() throws RestException;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestAction other = (RestAction) obj;
		if (actionName == null)
		{
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		if (context != other.context)
			return false;
		if (method != other.method)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "RestAction [context=" + context + ", method=" + method + ", actionName=" + actionName + "]";
	}
}
