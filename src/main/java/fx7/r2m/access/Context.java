package fx7.r2m.access;

public enum Context
{
	PLAYER, //
	WORLD, //
	SCRIPT;

	public String asContext()
	{
		return name().toLowerCase();
	}

	public static Context fromString(String context)
	{
		if (context == null)
			return null;

		try
		{
			return Context.valueOf(context);
		} catch (IllegalArgumentException e)
		{
			return null;
		}
	}
}
