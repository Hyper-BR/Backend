package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.CollectionRequestDTO;
import br.com.hyper.dtos.responses.CollectionResponseDTO;
import br.com.hyper.dtos.responses.pages.CollectionPageResponseDTO;
import br.com.hyper.entities.CollectionEntity;
import br.com.hyper.exceptions.CollectionNotFoundException;
import br.com.hyper.exceptions.InvalidCollectionDataException;
import br.com.hyper.repositories.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Autowired
    private final ModelMapper modelMapper;


    @Override
    public CollectionResponseDTO save(CollectionRequestDTO collection) {

        CollectionEntity collectionEntity;
        try {
            collectionEntity = modelMapper.map(collection, CollectionEntity.class);

            collectionEntity = collectionRepository.save(collectionEntity);

            return modelMapper.map(collectionEntity, CollectionResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new InvalidCollectionDataException(ErrorCodes.DUPLICATED_DATA,
                    ErrorCodes.DUPLICATED_DATA.getMessage());
        }
    }
    @Override
    public CollectionPageResponseDTO find(String name, Pageable pageable) {

        Page<CollectionEntity> collectionEntities;

        if(name != null){
            collectionEntities = collectionRepository.findByName(name, pageable);
        } else {
            collectionEntities = collectionRepository.findAll(pageable);
        }
        return modelMapper.map(collectionEntities, CollectionPageResponseDTO.class);
    }

    @Override
    public CollectionResponseDTO update(Long id, CollectionRequestDTO collection) {
        CollectionEntity collectionCurrent = findByIdOrThrowCollectionDataNotFoundException(id);

        collectionCurrent.setName(collection.getName());

        collectionRepository.save(collectionCurrent);

        return modelMapper.map(collectionCurrent, CollectionResponseDTO.class);
    }

    @Override
    public void delete(Long id) {
        CollectionEntity collectionCurrent = findByIdOrThrowCollectionDataNotFoundException(id);

        CollectionResponseDTO response = modelMapper.map(collectionCurrent, CollectionResponseDTO.class);
        collectionRepository.delete(collectionCurrent);
    }
    private CollectionEntity findByIdOrThrowCollectionDataNotFoundException(Long id) {
        return collectionRepository.findById(id).orElseThrow(
                () -> new CollectionNotFoundException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }
}
