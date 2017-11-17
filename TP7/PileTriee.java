
public class PileTriee<T extends Number & Comparable<? super T>> extends Pile<T>
{
	public PileTriee()
	{
		super();
	}

	protected PileTriee(T value, Pile<T> next)
	{
		super(value, next);
	}

	public void push(T value)
	{
		if (next == null)
			next = new PileTriee<T>(value, next);

		else
		{
			if (next.data.compareTo(value) > 0)
				next = new PileTriee<T>(value, next);

			else
				next.push(value);
		}
	}
}