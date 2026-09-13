package com.example.assembler;

import com.example.controller.UserController;
import com.example.dto.UserResponseHateoasDto;
import com.example.dto.UserResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler 
        extends RepresentationModelAssemblerSupport<UserResponseDto, UserResponseHateoasDto> {
    
    public UserModelAssembler() {
        super(UserController.class, UserResponseHateoasDto.class);
    }
    
    @Override
    public UserResponseHateoasDto toModel(UserResponseDto myUserResponseDto) {
        UserResponseHateoasDto myUserHateoasDto = new UserResponseHateoasDto(myUserResponseDto);
        
        Long myUserId = myUserResponseDto.getUserId();
        
        // Ссылкии HATEOAS
        myUserHateoasDto.add(linkTo(
            methodOn(UserController.class).myGetUserById(myUserId))
            .withSelfRel());
        
        myUserHateoasDto.add(linkTo(
            methodOn(UserController.class).myUpdateUser(myUserId, null))
            .withRel("update-user"));
        
        myUserHateoasDto.add(linkTo(
            methodOn(UserController.class).myDeleteUser(myUserId))
            .withRel("delete-user"));
        
        myUserHateoasDto.add(linkTo(
            methodOn(UserController.class).myGetAllUsers())
            .withRel("all-users"));
        
        myUserHateoasDto.add(linkTo(
            methodOn(UserController.class).myGetUserByEmail(myUserResponseDto.getUserEmail()))
            .withRel("user-by-email"));
        
        return myUserHateoasDto;
    }
    
    @Override
    public CollectionModel<UserResponseHateoasDto> toCollectionModel(
            Iterable<? extends UserResponseDto> myUserResponseDtos) {
        
        CollectionModel<UserResponseHateoasDto> myUserModels = super.toCollectionModel(myUserResponseDtos);
        
        // Общие ссылки для коллекции
        myUserModels.add(linkTo(methodOn(UserController.class).myGetAllUsers()).withSelfRel());
        myUserModels.add(linkTo(methodOn(UserController.class).myCreateUser(null)).withRel("create-user"));
        
        return myUserModels;
    }
}