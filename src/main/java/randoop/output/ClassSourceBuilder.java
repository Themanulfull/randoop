package randoop.output;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builds the source code for a (non-generic) class in a string.
 */
public class ClassSourceBuilder extends SourceBuilder {

  /** The name of the class */
  private final String classname;

  /** The package of the class */
  private String packageName;

  /** The imports for the class */
  private List<String> importNames;

  /** The annotations for the class */
  private List<String> classAnnotation;

  /** The members of the class */
  private List<List<String>> memberDeclarations;

  /**
   * Creates a {@link ClassSourceBuilder} for a class with the given name and package.
   *
   * @param classname  the name of the class declaration to be built
   * @param packageName  the name of the package for the class declaration
   */
  public ClassSourceBuilder(String classname, String packageName) {
    this.classname = classname;
    if (packageName == null) {
      this.packageName = "";
    } else {
      this.packageName = packageName;
    }
    this.importNames = new ArrayList<>();
    this.classAnnotation = new ArrayList<>();
    this.memberDeclarations = new ArrayList<>();
  }

  /**
   * Add import declarations to this class declaration.
   * Takes the name of the import (e.g., {@code java.util.List} or {@code java.util.*}.
   *
   * @param importNames  the list of import declarations to add to this class declaration
   */
  public void addImports(Collection<String> importNames) {
    if (importNames != null) {
      this.importNames.addAll(importNames);
    }
  }

  /**
   * Add annotations to this class declaration.
   *
   * @param annotations  the collection of annotations to be added to this declaration
   */
  public void addAnnotation(Collection<String> annotations) {
    if (annotations != null) {
      this.classAnnotation.addAll(annotations);
    }
  }

  public void addMember(String declarationString) {
    if (declarationString != null && !declarationString.isEmpty()) {
      List<String> declarationLines = new ArrayList<>();
      declarationLines.add(declarationString);
      memberDeclarations.add(declarationLines);
    }
  }

  public void addMember(List<String> declarationLines) {
    if (declarationLines != null && !declarationLines.isEmpty()) {
      memberDeclarations.add(declarationLines);
    }
  }

  @Override
  public List<String> toLines() {
    List<String> lines = new ArrayList<>();
    if (packageName != null && !packageName.isEmpty()) {
      lines.add(createLine("package", packageName, ";"));
      lines.add(createLine());
    }
    if (!importNames.isEmpty()) {
      for (String importName : importNames) {
        lines.add(createLine("import", importName, ";"));
      }
      lines.add(createLine());
    }
    if (!classAnnotation.isEmpty()) {
      for (String annotation : classAnnotation) {
        lines.add(createLine(annotation));
      }
    }

    lines.add(createLine("public", "class", classname, "{"));
    for (List<String> memberDeclaration : memberDeclarations) {
      for (String line : memberDeclaration) {
        lines.add(createLine(line));
      }
      lines.add(createLine());
    }
    lines.add(createLine("}"));
    return lines;
  }

  @Override
  public String toString() {
    String source = super.toString();
    try {
      return new Formatter().formatSource(source);
    } catch (FormatterException e) {
      System.out.println("Generated source has an error:");
      System.out.println(source);
      e.printStackTrace();
      //code generation bad -- internal error
      return null;
    }
  }
}
