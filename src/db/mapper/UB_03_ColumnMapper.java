package db.mapper;

import UB_03_query.valueobjects.UB_03_Sort.Column;
import db.entities.TaskEntity;
import mapping.framework.EntityMapper;

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
