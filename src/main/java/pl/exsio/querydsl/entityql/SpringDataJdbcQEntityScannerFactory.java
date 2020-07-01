package pl.exsio.querydsl.entityql;

import org.springframework.data.relational.core.mapping.NamingStrategy;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;
import pl.exsio.querydsl.entityql.entity.scanner.SpringDataJdbcQEntityScanner;
import pl.exsio.querydsl.entityql.jdbc.DefaultNamingStrategy;

import java.util.Map;
import java.util.Optional;

public class SpringDataJdbcQEntityScannerFactory implements QEntityScannerFactory {

    private final static String NAMING_STRATEGY = "namingStrategy";

    private final static String DEFAULT_NAMING_STRATEGY = DefaultNamingStrategy.class.getName();

    @Override
    public QEntityScanner createScanner(Map<String, String> params) throws Exception {
        return new SpringDataJdbcQEntityScanner(getNamingStrategy(params));
    }

    private NamingStrategy getNamingStrategy(Map<String, String> params) throws Exception {
        String namingStrategyClass = Optional.ofNullable(params.get(NAMING_STRATEGY)).orElse(DEFAULT_NAMING_STRATEGY);
        try {
            return (NamingStrategy) Class.forName(namingStrategyClass).newInstance();
        } catch (Exception ex) {
            throw new Exception(String.format("Unable to instantiate Naming Strategy: %s", namingStrategyClass), ex);
        }
    }
}
