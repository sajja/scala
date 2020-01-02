package com.example.groovy;

import akka.parboiled2.Rule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.transform.ThreadInterrupt;
import groovy.transform.TimedInterrupt;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class GS implements Runnable {
    public static void xxx() {
        Map<String, Object> timeoutArgs = ImmutableMap.<String, Object>of("value", 1);
        ASTTransformationCustomizer customizer = new ASTTransformationCustomizer(timeoutArgs, TimedInterrupt.class);
        CompilerConfiguration config = new CompilerConfiguration();
        config.addCompilationCustomizers(customizer);
//        Map<String, Object> bindingMap = parameters == null ? Collections.<String, Object>emptyMap() : parameters;
        //copy map to ensure that binding is mutable (for use in registerAliases)
//        Binding binding = new Binding(Maps.newHashMap(bindingMap));
        GroovyShell shell = new GroovyShell(config);
        Object o = shell.evaluate("import org.crsh.command.*;" +
                "java.lang.Thread.sleep(1000);" +
                "return   100");
        System.out.println(o);
    }

    public static void yyy() {
        Map<String, Object> timeoutArgs = ImmutableMap.<String, Object>of("value", 2);
        SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
        secureASTCustomizer.setClosuresAllowed(true);
        secureASTCustomizer.setMethodDefinitionAllowed(false);
        secureASTCustomizer.setImportsWhitelist(Arrays.asList(new String[]{}));
        secureASTCustomizer.setStarImportsWhitelist(Arrays.asList(
                new String[]{"org.crsh.command.*", "org.crsh.cli.*", "org.crsh.groovy.*",
                        "com.ge.predix.acs.commons.policy.condition.*"}));
        secureASTCustomizer.setConstantTypesClassesWhiteList(Arrays.asList(
                new Class[]{Thread.class, Boolean.class, boolean.class, Collection.class, Double.class, double.class, Float.class,
                        float.class, Integer.class, int.class, Long.class, long.class, Object.class, String.class}));
        secureASTCustomizer.setReceiversClassesWhiteList(Arrays.asList(
                new Class[]{Thread.class, Boolean.class, Collection.class, Integer.class, Iterable.class, Object.class, Set.class,
                        String.class}));
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.addCompilationCustomizers(secureASTCustomizer);


        GroovyShell shell = new GroovyShell(conf);
        Object o = shell.evaluate("import org.crsh.command.*;" +
                "java.lang.Thread.sleep(1000);" +
                "return   100");
        System.out.println(o);
    }

    public static void main(String[] args) throws InterruptedException {
//        xxx();
        GS x = new GS();
        Thread t = new Thread(x);
        t.start();

        Thread.sleep(1000);
        System.out.println("Interuupting");
        t.interrupt();

        Thread.sleep(100000);


    }

    @Override
    public void run() {
        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
        compilerConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        compilerConfig.addCompilationCustomizers(new SecureASTCustomizer());
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, compilerConfig);
        Object o = shell.evaluate("import org.crsh.command.*;" +
                "byte[] b = new byte[100];System.in.read(b);" +
//                "while(true) println(\"x\");" +
                "System.out.println(\"Groovy interrupt\");" +
                "return  100");
        System.out.println(o);
    }
}

