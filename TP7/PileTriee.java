
public class PileTriee extends Pile<Double>
{
	public PileTriee()
	{
		super();
	}

	protected PileTriee(Double value, Pile<Double> next)
	{
		super(value, next);
	}

	public void push(Double value)
	{
		if (next == null || next.data >= value)
			next = new PileTriee(value, next);

		else
			next.push(value);
	}
}