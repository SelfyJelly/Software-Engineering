package de.stuttgart.hft.organizer.layer4.db.mapper;

import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Sort.Column;
import de.stuttgart.hft.organizer.layer4.db.entities.TaskEntity;
import de.stuttgart.hft.organizer.layer4.mapping.framework.EntityMapper;

public class UB_03_ColumnMapper implements EntityMapper<Column, String> {
	
	@Override
	public String toEntity(Column column) {
		return switch(column) {
			case ID				-> TaskEntity.ID;
			case TITLE			-> TaskEntity.TITLE;
			case ENTRY_DATE		-> TaskEntity.ENTRY_DATE;
			case ESTIMATE		-> TaskEntity.ESTIMATE;
			case DUE_DATE		-> TaskEntity.DUE_DATE;
			case PRIORITY		-> TaskEntity.PRIORITY;
			case DONE			-> TaskEntity.DONE;
			case DESCRIPTION	-> TaskEntity.DESCRIPTION;
			default -> null;		
		};
	}
}
