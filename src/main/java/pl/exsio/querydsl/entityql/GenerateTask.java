package pl.exsio.querydsl.entityql;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.data.annotation.Id;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import javax.persistence.Entity;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class GenerateTask extends DefaultTask {

    private static final EnumMap<Generator.Type, QEntityScannerFactory> SCANNERS = new EnumMap<>(Generator.Type.class);
    private static final EnumMap<Generator.Type, BiFunction<Generator, URLClassLoader, Set<Class<?>>>> REFLECTION_SCANNERS
            = new EnumMap<>(Generator.Type.class);

    private GeneratePluginExtension extension;
    private Set<File> sourcePaths;
    private File baseDir;

    static {
        SCANNERS.put(Generator.Type.JPA, new JPAQEntityScannerFactory());
        SCANNERS.put(Generator.Type.SPRING_DATA_JDBC, new SpringDataJdbcQEntityScannerFactory());
        REFLECTION_SCANNERS.put(Generator.Type.JPA, GenerateTask::resolveJpaEntityClasses);
        REFLECTION_SCANNERS.put(Generator.Type.SPRING_DATA_JDBC, GenerateTask::resolveJdbcEntityClasses);
    }

    private final QExporter exporter = new QExporter();

    void setExtension(GeneratePluginExtension extension) {
        this.extension = extension;
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    @TaskAction
    public void generateModel() throws Exception {
        getLogger().info("Running EntityQL Model generation...");
        URLClassLoader classLoader = classLoader();
        for (Generator generator : getGenerators()) {
            generate(generator, classLoader);
        }
    }

    private void generate(Generator generator, URLClassLoader classLoader) throws Exception {
        QEntityScanner scanner = SCANNERS.get(generator.getType()).createScanner(generator.getParams());
        getLogger().info("Using scanner: {}", scanner.getClass().getName());
        generator.setDefaultDestinationPathIfNeeded(baseDir.getAbsolutePath());
        getLogger().info("Generating EntityQL Static Models from package {} to package {}, destination path: {}",
                generator.getSourcePackage(), generator.getDestinationPackage(), generator.getDestinationPath()
        );
        Set<Class<?>> entityClasses = REFLECTION_SCANNERS.get(generator.getType()).apply(generator, classLoader);
        for (String sourceClass : generator.getSourceClasses()) {
            entityClasses.add(loadClass(classLoader, sourceClass));
        }
        getLogger().info("Found {} Entity Classes to export in package {}", entityClasses.size(),
                generator.getSourcePackage());
        for (Class<?> entityClass : entityClasses) {
            getLogger().info("Exporting class: {}", entityClass.getName());
            exporter.export(
                    EntityQL.qEntity(entityClass, scanner),
                    generator.getFilenamePattern(),
                    generator.getDestinationPackage(),
                    generator.getDestinationPath()
            );
        }
    }

    private URLClassLoader classLoader() throws Exception {
        List<URL> projectClasspathList = new ArrayList<>();
        for (File element : sourcePaths) {
            getLogger().info("Adding element to Class Loader: {}", element);
            try {
                projectClasspathList.add(element.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new Exception(element + " is an invalid classpath element", e);
            }
        }
        return new URLClassLoader(projectClasspathList.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader());
    }

    private Class<?> loadClass(URLClassLoader classLoader, String sourceClass) throws Exception {
        return Class.forName(sourceClass, true, classLoader);
    }

    public void setSourcePaths(Set<File> sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    @Internal
    public List<Generator> getGenerators() {
        return extension.getGenerators();
    }

    private static Set<Class<?>> resolveJpaEntityClasses(Generator generator,
                                                         URLClassLoader classLoader) {
        Reflections reflections = new Reflections(generator.getSourcePackage(), classLoader);
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    private static Set<Class<?>> resolveJdbcEntityClasses(Generator generator, URLClassLoader classLoader) {
        Reflections reflections = new Reflections(generator.getSourcePackage(), classLoader, new FieldAnnotationsScanner());
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Id.class);
        return fieldsAnnotatedWith.stream().map(Field::getDeclaringClass).collect(
                Collectors.toSet());
    }
}
