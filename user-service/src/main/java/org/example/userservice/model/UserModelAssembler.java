package org.example.userservice.model;

import org.example.userservice.controller.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto dto) {
        return EntityModel.of(dto,
                WebMvcLinkBuilder.linkTo(UserController.class).withRel("users"),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).updateUser(dto)).withRel("update"),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("delete")
        );
    }
}
