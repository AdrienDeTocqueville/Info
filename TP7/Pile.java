
public class Pile<T>
{
	protected T data;
	protected Pile<T> next;

	public Pile()
	{
		data = null;
		next = null;
	}

	protected Pile(T value, Pile<T> next)
	{
		this.data = value;
		this.next = next;
	}

	public void push(T value)
	{
		next = new Pile<T>(value, next);
	}

	public T peek()
	{
		if (next == null)
			return null;
		
		else
			return next.data;
	}

	public T pop()
	{
		if (next == null)
			return null;

		T copy = next.data;
		next = next.next;
			
		return copy;
	}

	public int count()
	{
		if (next == null)
			return 0;

		else
			return next.count() + 1;
	}

	public int countIter()
	{
		Pile<T> current = this;
		int size = -1;

		while (current != null)
		{
			size++;
			current = current.next;
		}
		
		return size;
	}
}
