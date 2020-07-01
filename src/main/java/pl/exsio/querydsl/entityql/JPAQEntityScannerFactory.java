package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.entity.scanner.JpaQEntityScanner;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import java.util.Map;

public class JPAQEntityScannerFactory implements QEntityScannerFactory {
    @Override
    public QEntityScanner createScanner(Map<String, String> params) {
        return new JpaQEntityScanner();
    }
}
