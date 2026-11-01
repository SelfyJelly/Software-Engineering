package mapping.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {
	
	E toEntity(D domain);
	
	default List<E> toEntity(List<D> domainElements){
		return domainElements.stream().map(d -> toEntity(d)).collect(Collectors.toCollection(ArrayList::new));
	}
}