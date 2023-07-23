package me.syncwrld.cmdextensor.framework;

public class PropertiesNotFoundException extends RuntimeException {

    PropertiesNotFoundException(String classname) {
        super("CommandBuilder annotation not found in current class: " + classname + ". Check the wiki for help or simply add @CommandBuilder annotation in scope of your class.");
    }

}
