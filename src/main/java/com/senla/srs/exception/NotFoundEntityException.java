package com.senla.srs.exception;

import javassist.NotFoundException;

public class NotFoundEntityException extends NotFoundException {

    private static final String NO_CLASS = "No class ";

    public NotFoundEntityException(Class<?> clazz){
        super(NO_CLASS + clazz.getName() + " entity with this id/name exists!");
    }

    public NotFoundEntityException(Class<?> clazz, Long id){
        super(NO_CLASS + clazz.getName() + " entity with id " + id + " exists!");
    }

    public NotFoundEntityException(Class<?> clazz, String name){
        super(NO_CLASS + clazz.getName() + " entity with name " + name + " exists!");
    }

}
