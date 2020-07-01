package pl.exsio.querydsl.entityql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {

    enum Type {
        JPA, SPRING_DATA_JDBC
    }

    private final static String SEPARATOR = System.getProperty("file.separator");

    private Type type = Type.JPA;

    private Map<String, String> params = new HashMap<>();

    private String sourcePackage;

    private String destinationPackage;

    private List<String> sourceClasses;

    private String filenamePattern = "Q%s.java";

    private String destinationPath;


    public Generator() {
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public String getDestinationPackage() {
        return destinationPackage;
    }

    public void setDestinationPackage(String destinationPackage) {
        this.destinationPackage = destinationPackage;
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getSourceClasses() {
        if(sourceClasses == null) {
            return new ArrayList<>();
        }
        return sourceClasses;
    }

    public void setSourceClasses(List<String> sourceClasses) {
        this.sourceClasses = sourceClasses;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    void setDefaultDestinationPathIfNeeded(String baseDir) {
        if (destinationPath == null) {
            destinationPath = baseDir + SEPARATOR + ".." + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "java";
        }
    }

    @Override
    public String toString() {
        return "Generator{" +
                "sourcePackage='" + sourcePackage + '\'' +
                ", destinationPackage='" + destinationPackage + '\'' +
                ", filenamePattern='" + filenamePattern + '\'' +
                ", destinationPath='" + destinationPath + '\'' +
                ", type='" + type + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}