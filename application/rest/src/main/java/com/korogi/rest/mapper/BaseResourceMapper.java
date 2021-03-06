package com.korogi.rest.mapper;

import java.util.List;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public abstract class BaseResourceMapper<TO, FROM> implements ResourceMapper<TO, FROM> {
    private final Class<FROM> fromClass;

    public BaseResourceMapper(Class<FROM> fromClass) {
        this.fromClass = fromClass;
    }

    @Override
    public EntityModel<TO> toDTOResource(FROM from) {
        return EntityModel.of(map(from), createLinks(from));
    }

    @Override
    public Class<FROM> fromObjectClass() {
        return fromClass;
    }

    protected abstract TO map(FROM from);

    protected abstract List<Link> createLinks(FROM from);
}