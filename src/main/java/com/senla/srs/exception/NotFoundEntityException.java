package com.senla.srs.exception;

import javassist.NotFoundException;

public class NotFoundEntityException extends NotFoundException {
    public NotFoundEntityException(String entityName) {
        super(entityName + " with this ID not found");
    }
}
