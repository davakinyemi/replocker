package com.ap2.replocker.exception.custom;

import lombok.*;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
public class CollectionNotFoundException extends RuntimeException {
    private final UUID collectionId;

    public CollectionNotFoundException(UUID collectionId) {
        this.collectionId = collectionId;
    }
    public CollectionNotFoundException(UUID collectionId, String message) {
        super(message + " - " + collectionId);
        this.collectionId = collectionId;
    }

}
