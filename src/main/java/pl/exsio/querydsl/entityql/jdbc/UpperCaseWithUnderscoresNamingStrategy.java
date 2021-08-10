package pl.exsio.querydsl.entityql.jdbc;

import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

import static pl.exsio.querydsl.entityql.util.NamingUtil.camelToUnderscore;

public class UpperCaseWithUnderscoresNamingStrategy implements NamingStrategy {

    @Override
    public String getTableName(Class<?> type) {
        return camelToUnderscore(type.getSimpleName()) + "S";
    }

    @Override
    public String getColumnName(RelationalPersistentProperty property) {

        String propertyInLowerUnderscore = camelToUnderscore(property.getName());

        if (property.isIdProperty()) {
            return property.getOwner().getType().getSimpleName().toUpperCase() + "_" + propertyInLowerUnderscore;
        }

        return propertyInLowerUnderscore;
    }

    @Override
    public String getReverseColumnName(PersistentPropertyPathExtension path) {
        return path.getIdColumnName().getReference();
    }
}
