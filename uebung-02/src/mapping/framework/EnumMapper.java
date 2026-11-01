package mapping.framework;

public class EnumMapper<T extends Enum<T>> {

	private T[] enums;

	private EnumMapper() {
	}

	@SafeVarargs
	public static <T extends Enum<T>> EnumMapper<T> of(T... ts) {
		EnumMapper<T> mapper = new EnumMapper<>();
		mapper.enums = ts;
		return mapper;
	}

	public int toInt(T t) {
		for (int i = 0; i < enums.length; i++)
			if (enums[i] == t)
				return i;
		throw new IllegalArgumentException(t + " not found");
	}

	public T toEnum(int n) {
		if (n < 0 || n >= enums.length)
			throw new IllegalArgumentException("Value " + n + " not found");
		return enums[n];
	}
}
