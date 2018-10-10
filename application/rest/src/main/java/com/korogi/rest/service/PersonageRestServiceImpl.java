package com.korogi.rest.service;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.korogi.api.PersonageRestService;
import com.korogi.core.domain.Personage;
import com.korogi.core.persistence.personage.PersonageRepository;
import com.korogi.dto.PersonageDTO;
import com.korogi.rest.exception.ResourceNotFoundException;
import com.korogi.rest.mapper.EntityToDTOResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/personages", produces = { MediaType.APPLICATION_JSON_VALUE })
@Transactional(readOnly = true)
public class PersonageRestServiceImpl implements PersonageRestService {
    private static final String PATH_VARIABLE_ID = "id";

    private final PersonageRepository personageRepository;
    private final EntityToDTOResourceMapper entityToDTOResourceMapper;

    @Autowired
    public PersonageRestServiceImpl(
            PersonageRepository personageRepository,
            EntityToDTOResourceMapper entityToDTOResourceMapper
    ) {
        this.personageRepository = personageRepository;
        this.entityToDTOResourceMapper = entityToDTOResourceMapper;
    }

    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    @Override
    public @ResponseBody PagedResources<Resource<PersonageDTO>> getPersonages() {
        return null;
    }

    @RequestMapping(value = "{" + PATH_VARIABLE_ID + "}", method = GET)
    @ResponseStatus(OK)
    @Override
    public @ResponseBody Resource<PersonageDTO> getPersonageDetails(
            @PathVariable(PATH_VARIABLE_ID) Long id
    ) {
        Personage personage = personageRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return entityToDTOResourceMapper.toDTOResource(personage);
    }
}