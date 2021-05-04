package com.senla.srs.exception;

import javassist.NotFoundException;

public class NotFoundEntityException extends NotFoundException {
    public NotFoundEntityException(Class<?> clazz){
        super("No class " + clazz.getName() + " entity with this id/name exists!");
    }

    public NotFoundEntityException(Class<?> clazz, Long id){
        super("No class " + clazz.getName() + " entity with id " + id + " exists!");
    }

    public NotFoundEntityException(Class<?> clazz, String name){
        super("No class " + clazz.getName() + " entity with name " + name + " exists!");
    }
}
