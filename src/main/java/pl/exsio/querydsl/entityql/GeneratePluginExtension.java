package pl.exsio.querydsl.entityql;

import groovy.lang.Closure;
import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

public class GeneratePluginExtension {

    private final Project project;

    private List<Generator> generators = new ArrayList<>();

    public GeneratePluginExtension(Project project) {
        this.project = project;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public void setGenerators(List<Closure<?>> closures) {
        closures.forEach(closure -> generators.add((Generator) project.configure(new Generator(), closure)));
    }

    public void setGenerator(Closure<?> closure) {
    }
}
