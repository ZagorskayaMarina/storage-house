package com.incorparation.mapper;

import com.incorparation.dto.StorageObject;
import com.incorparation.model.Storage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    StorageObject.StorageDTO storageToDto(Storage storage);
    Storage DtoToStorage(StorageObject.StorageDTO storageDTO);
}
