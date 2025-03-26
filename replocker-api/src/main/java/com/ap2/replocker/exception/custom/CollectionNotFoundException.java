package com.ap2.replocker.exception.custom;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException(UUID collectionId) {
        super(collectionId.toString());
    }
}
