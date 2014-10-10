package org.example.order;

import org.dozer.DozerConverter;
import org.example.order.abc.Status;
import org.example.order.xyz.Priority;


public class StatusConverter extends DozerConverter<Status, Priority> {
    
    public StatusConverter() {
        super(Status.class, Priority.class);
    }

    @Override
    public Priority convertTo(
            Status source,
            Priority destination) {
        switch (source) {
        case GOLD :
            return Priority.HIGH;
        case NORMAL :
            return Priority.MEDIUM;
        case VALUE :
            return Priority.LOW;
        default :
            return null;
        }
    }

    @Override
    public Status convertFrom (
            Priority source, 
            Status destination) {
        
        return null;
    }

}
