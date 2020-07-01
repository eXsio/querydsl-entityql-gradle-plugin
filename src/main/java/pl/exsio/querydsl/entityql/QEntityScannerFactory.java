package pl.exsio.querydsl.entityql;

import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import java.util.Map;

public interface QEntityScannerFactory {

    QEntityScanner createScanner(Map<String, String> params) throws Exception;
}
