package com.example;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import com.example.domain.Entity;
import com.example.domain.EntityField;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.base.CaseFormat;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.MethodInvocationException;

public class BootCodeGenerator {

    public static Entity generateEntityObject(String inputfile) throws Exception {

        Entity phoneCounsellingEntity = Entity.builder().entityName("PhoneCouselling").entityNameSmall("phoneCounselling").build();

        Path textFile = Paths.get(BootCodeGenerator.class.getResource(inputfile).toURI());
        List<EntityField> entityFieldList = new ArrayList<>();
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(textFile)) {

            stream.forEach(line -> {
                String[] components = line.split(" ");
                String camelCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, components[0]);
                String type = getTypeInfo(components[1]);
                EntityField field = EntityField.builder().name(camelCase).value(components[2]).type(type).build();
                entityFieldList.add(field);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        phoneCounsellingEntity.setEntityFieldList(entityFieldList);
        return phoneCounsellingEntity;
    }

    public static String getTypeInfo(String input){
        if ( input.equals("VARCHAR") ){
            return "String";
        }
        return "";
    }

    public static void generateControllerTest(Entity phoneCounsellingEntity) throws Exception {
        VelocityContext context = new VelocityContext();

        context.put( "entity", phoneCounsellingEntity );

        Template template = null;

        try
        {
            template = Velocity.getTemplate("controllerTest.vm");
        }
        catch( Exception e )
        {}

        StringWriter sw = new StringWriter();

        template.merge( context, sw );

        //System.out.println(sw.toString());


        CompilationUnit compilationUnit = StaticJavaParser.parse(sw.toString());
        System.out.println(compilationUnit.toString());

        writeFile("ControllerTest.java", compilationUnit.toString());

    }

    public static void writeFile(String filePath, String contents) throws Exception {
        Path path = Paths.get(filePath);
        Files.write(path, contents.getBytes());
    }

    public static void generateController(Entity phoneCounsellingEntity) throws Exception {
        VelocityContext context = new VelocityContext();

        context.put( "entity", phoneCounsellingEntity );

        Template template = null;

        try
        {
            template = Velocity.getTemplate("controller.vm");
        }
        catch( Exception e )
        {}

        StringWriter sw = new StringWriter();

        template.merge( context, sw );

        //System.out.println(sw.toString());


        CompilationUnit compilationUnit = StaticJavaParser.parse(sw.toString());
        System.out.println(compilationUnit.toString());

        writeFile("Controller.java", compilationUnit.toString());
    }

    public static void main(String[] args) throws Exception {

        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");


        Velocity.init(p);

        Entity phoneCounsellingEntity = BootCodeGenerator.generateEntityObject("/Input.txt");

        generateControllerTest(phoneCounsellingEntity);
        generateController(phoneCounsellingEntity);

    }


}
