package fx7.r2m.entity;

import fx7.r2m.access.Context;

public abstract class ContextEntity implements Entity, Comparable<ContextEntity>
{
	private Context context;
	private String entityName;

	protected ContextEntity(Context context, String entityName)
	{
		this.context = context;
		this.entityName = entityName;
	}

	public String getEntityName()
	{
		return entityName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
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
		ContextEntity other = (ContextEntity) obj;
		if (context != other.context)
			return false;
		if (entityName == null)
		{
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		return true;
	}

	@Override
	public int compareTo(ContextEntity o)
	{
		return getEntityName().compareTo(o.getEntityName());
	}

	@Override
	public String toString()
	{
		return "ContextEntity [context=" + context + ", entityName=" + entityName + "]";
	}
}
