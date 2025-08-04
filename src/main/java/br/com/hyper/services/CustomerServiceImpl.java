package br.com.hyper.services;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.utils.LocalFileStorageUtil;
import br.com.hyper.utils.PaginationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    private final PaginationMapper paginationMapper;

    @Override
    public CustomerResponseDTO findByEmail(String email) {

        CustomerEntity customerEntity = findByEmailOrThrowUserDataNotFoundException(email);

        return modelMapper.map(customerEntity, CustomerResponseDTO.class);

    }

    @Override
    public PageResponseDTO<CustomerResponseDTO> findAll(Pageable pageable) {

        Page<CustomerEntity> customerEntities = customerRepository.findAll(pageable);

        return paginationMapper.map(customerEntities, CustomerResponseDTO.class);
    }

    @Override
    public CustomerResponseDTO update(UUID id, CustomerRequestDTO dto, CustomerEntity customer){

        if (!customer.getId().equals(id)) {
            throw new GenericException(ErrorCodes.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED.getMessage());
        }

        if(dto.getName() != null && !dto.getName().isEmpty()) {
            customer.setName(dto.getName());
        }

        if(dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
            customer.setBirthDate(dto.getBirthDate());
        }

        if(dto.getCountry() != null && !dto.getCountry().isEmpty()) {
            customer.setCountry(dto.getCountry());
        }

        if(dto.getBiography() != null && !dto.getBiography().isEmpty()) {
            customer.setBiography(dto.getBiography());
        }

        if(dto.isRemoveAvatar()) {
            customer.setAvatarUrl(BaseUrls.AVATAR_URL);
        } else if(dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            String avatarUrl = LocalFileStorageUtil.saveFile(dto.getAvatar(), customer.getId().toString(),"", "avatar");
            customer.setAvatarUrl(avatarUrl);
        }

        if (dto.isRemoveCover()) {
            customer.setCoverUrl(null);
        } else if(dto.getCover() != null && !dto.getCover().isEmpty()) {
            String coverUrl = LocalFileStorageUtil.saveFile(dto.getCover(), customer.getId().toString(),"", "cover");
            customer.setCoverUrl(coverUrl);
        }

        customer = customerRepository.save(customer);

        return modelMapper.map(customer, CustomerResponseDTO.class);
    }


    @Override
    public void delete(UUID id) {
        CustomerEntity userCurrent = findByIdOrThrowUserDataNotFoundException(id);

        modelMapper.map(userCurrent, CustomerResponseDTO.class);
        customerRepository.delete(userCurrent);
    }

    private CustomerEntity findByIdOrThrowUserDataNotFoundException(UUID id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

    private CustomerEntity findByEmailOrThrowUserDataNotFoundException(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
