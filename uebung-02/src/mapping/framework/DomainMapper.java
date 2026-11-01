package mapping.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface DomainMapper<D, E> {
	
	D toDomain(E entity);

	default List<D> toDomain(List<E> entities){
		return entities.stream().map(e -> toDomain(e)).collect(Collectors.toCollection(ArrayList::new));
	}
}