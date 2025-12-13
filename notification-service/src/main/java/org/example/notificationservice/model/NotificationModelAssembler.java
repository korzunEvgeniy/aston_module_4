package org.example.notificationservice.model;

import org.example.notificationservice.controller.NotificationController;
import org.example.notificationservice.service.UserEvent;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<UserEvent, EntityModel<UserEvent>> {

    @Override
    public EntityModel<UserEvent> toModel(UserEvent event) {
        return EntityModel.of(event,
                WebMvcLinkBuilder.linkTo(NotificationController.class).withRel("notifications"),
                WebMvcLinkBuilder.linkTo(methodOn(NotificationController.class)
                                .sendEmail(event.email(), event.operation()))
                        .withSelfRel()
        );
    }
}
