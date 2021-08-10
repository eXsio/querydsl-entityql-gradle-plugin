# Static Code Generation for QueryDSL EntityQL 

[![Build Status](https://www.travis-ci.com/eXsio/querydsl-entityql-gradle-plugin.svg?branch=master)](https://www.travis-ci.com/eXsio/querydsl-entityql-gradle-plugin.svg?branch=master)
[![](https://jitpack.io/v/eXsio/querydsl-entityql-gradle-plugin.svg)](https://jitpack.io/#eXsio/querydsl-entityql-gradle-plugin)

This repository is a part of [EntityQL](https://github.com/eXsio/querydsl-entityql) project.
It is a Gradle Plugin that generates QueryDSL-SQL compatible Static Models from JPA Entities. 

## Generating JPA Models:

- add the following configuration to your ```settings.gradle```:

```groovy

    pluginManagement {
        resolutionStrategy {
            eachPlugin {
                if (requested.id.id == "pl.exsio.querydsl.entityql") {
                    useModule("com.github.eXsio:querydsl-entityql-gradle-plugin:${requested.version}")
                }
            }
        }
        repositories {
            gradlePluginPortal()
            mavenLocal()
            mavenCentral()
            maven {
                url = uri('https://jitpack.io')
            }
        }
    }

```
 
- add the following plugin to your ```build.gradle```:

```groovy

    plugins {
        
        id 'pl.exsio.querydsl.entityql' version "0.0.10"
    }

```

- add the following configuration to your ```build.gradle```:

```groovy

    entityql {
        generators = [
            generator = {
                type = 'JPA'
                sourcePackage = 'pl.exsio.querydsl.entityql.examples.jpa.entity'
                destinationPackage = 'pl.exsio.querydsl.entityql.examples.jpa.entity.generated'
                //below parameters are optional and will be set with the following default values:
                //destinationPath = new StringBuilder(project.projectDir.absolutePath).append('/src/main/java').toString()
                //filenamePattern = 'Q%s.java'
            }
        ]
    }

```

- generate Static Models by running the ```generateModels``` task from Command Line or your IDE

## Generating Spring Data JDBC Models:

- add the following configuration to your ```settings.gradle```:

```groovy

    pluginManagement {
        resolutionStrategy {
            eachPlugin {
                if (requested.id.id == "pl.exsio.querydsl.entityql") {
                    useModule("com.github.eXsio:querydsl-entityql-gradle-plugin:${requested.version}")
                }
            }
        }
        repositories {
            gradlePluginPortal()
            mavenLocal()
            mavenCentral()
            maven {
                url = uri('https://jitpack.io')
            }
        }
    }

```
 
- add the following plugin to your ```build.gradle```:

```groovy

    plugins {
        
        id 'pl.exsio.querydsl.entityql' version "0.0.10"
    }

```

- add the following configuration to your ```build.gradle```:

```groovy

    entityql {
        generators = [
            generator = {
                type = 'SPRING_DATA_JDBC'
                sourcePackage = 'pl.exsio.querydsl.entityql.examples.jpa.entity'
                destinationPackage = 'pl.exsio.querydsl.entityql.examples.jpa.entity.generated'
                //use any naming strategy of your choice that is available on classpath
                params = [
                        namingStrategy: 'pl.exsio.querydsl.entityql.jdbc.UpperCaseWithUnderscoresNamingStrategy'
                ]
                //below parameters are optional and will be set with the following default values:
                //destinationPath = new StringBuilder(project.projectDir.absolutePath).append('/src/main/java').toString()
                //filenamePattern = 'Q%s.java'
            }
        ]
    }

```

- generate Static Models by running the ```generateModels``` task from Command Line or your IDE

## Using Models:

- add the following configuration to your ```build.gradle```:

```groovy

repositories {
    maven {
        url = uri('https://jitpack.io')
    }
}

dependencies {
    implementation 'com.github.eXsio:querydsl-entityql:3.0.4'
}

- configure QueryDSL:

```java

@Bean
public SQLTemplates sqlTemplates() {
    //choose the implementation that matches your database engine
    return new H2Templates(); 
}

@Bean
public SQLQueryFactory queryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
    //last param is an optional varargs String with all the java.lang.Enum packages that you use in your Entities
    return new EntityQlQueryFactory(new Configuration(sqlTemplates), dataSource, "your.enums.package");
}

```

- Create SQL Queries with generated models:

```java

//obtain instances of generated models
 QBook book = QBook.INSTANCE; 
 QOrder order = QOrder.INSTANCE;
 QOrderItem orderItem = QOrderItem.INSTANCE;

//use them by creating and executing a Native Query using QueryDSL API
Long count = queryFactory.select(count())
                .from(
                        select(
                                 book.name, 
                                 order.id
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.book, book)
                        .innerJoin(orderItem.order, order)
                        .where(book.price.gt(new BigDecimal("80")))
                        .groupBy(book.category) 
                ).fetchOne();
```


## Examples

Feel free to browse the [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) to find out how to use EntityQL in your code.
