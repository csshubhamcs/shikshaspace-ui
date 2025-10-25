package com.shikshaspace.shikshaspaceui.component;

import com.shikshaspace.shikshaspaceui.dto.SpaceResponse;
import com.shikshaspace.shikshaspaceui.security.SecurityUtils;
import com.shikshaspace.shikshaspaceui.service.SpaceService;
import com.shikshaspace.shikshaspaceui.views.auth.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class TopicCard extends Div {

  private final SecurityUtils securityUtils;
  private final SpaceService spaceService;
  private final SpaceResponse space;

  public TopicCard(SpaceResponse space, SecurityUtils securityUtils, SpaceService spaceService) {
    this.space = space;
    this.securityUtils = securityUtils;
    this.spaceService = spaceService;

    addClassName("topic-card");

    Span titleSpan = new Span(space.getTitle());
    titleSpan.addClassName("topic-card__title");

    Span subtitleSpan = new Span(space.getSubtitle() != null ? space.getSubtitle() : "");
    subtitleSpan.addClassName("topic-card__subtitle");

    Span authorSpan = new Span(space.getHostUsername() != null ? space.getHostUsername() : "");
    authorSpan.addClassName("topic-card__author");

    Span timeSpan = new Span("Today 8:00 PM");
    timeSpan.addClassName("topic-card__time");

    Button joinButton = new Button("Join");
    joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    joinButton.addClassName("topic-card__button");
    joinButton.addClickListener(e -> handleJoin());

    add(titleSpan, subtitleSpan, authorSpan, timeSpan, joinButton);
  }

  private void handleJoin() {
    if (!securityUtils.isUserLoggedIn()) {
      Notification notification = Notification.show("Please login to join this space");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
      notification.setPosition(Notification.Position.TOP_CENTER);

      getUI().ifPresent(ui -> ui.navigate(LoginView.class));
      return;
    }

    if (space.getId() != null) {
      try {
        spaceService.joinSpace(space.getId());
        Notification notification = Notification.show("Successfully joined!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
      } catch (Exception e) {
        Notification notification = Notification.show("Failed to join: " + e.getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
      }
    }
  }
}
