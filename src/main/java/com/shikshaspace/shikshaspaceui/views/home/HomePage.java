package com.shikshaspace.shikshaspaceui.views.home;

import com.shikshaspace.shikshaspaceui.component.TopicCard;
import com.shikshaspace.shikshaspaceui.components.NavBar;
import com.shikshaspace.shikshaspaceui.dto.SpaceResponse;
import com.shikshaspace.shikshaspaceui.security.SecurityUtils;
import com.shikshaspace.shikshaspaceui.service.SpaceService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@Route("")
@PageTitle("Home - ShikshaSpace")
@PermitAll
public class HomePage extends VerticalLayout {

  private final SpaceService spaceService;
  private final SecurityUtils securityUtils;

  public HomePage(SpaceService spaceService, SecurityUtils securityUtils) {
    this.spaceService = spaceService;
    this.securityUtils = securityUtils;

    setPadding(false);
    setSpacing(false);
    setSizeFull();

    NavBar navBar = new NavBar(securityUtils);

    Div contentWrapper = new Div();
    contentWrapper.addClassName("home__content");

    H1 welcomeTitle = new H1("Welcome to ShikshaSpace");
    welcomeTitle.addClassName("home__title");

    Div userInfoCard = createUserInfoCard();

    Div topicsGrid = createTopicsGrid();

    contentWrapper.add(welcomeTitle, userInfoCard, topicsGrid);

    add(navBar, contentWrapper);
  }

  private Div createUserInfoCard() {
    Div card = new Div();
    card.addClassName("home__user-card");

    String username = securityUtils.getAuthenticatedUsername();
    if (username != null) {
      card.setText("Welcome, " + username);
    } else {
      card.setText("Create ShikshaSpace");
    }

    return card;
  }

  private Div createTopicsGrid() {
    Div grid = new Div();
    grid.addClassName("home__topics-grid");

    List<SpaceResponse> spaces = spaceService.getAllSpaces();

    if (spaces != null && !spaces.isEmpty()) {
      spaces.forEach(space -> grid.add(new TopicCard(space, securityUtils, spaceService)));
    } else {
      for (int i = 0; i < 6; i++) {
        grid.add(
            new TopicCard(
                SpaceResponse.builder()
                    .title("Sample Topic " + (i + 1))
                    .subtitle("Sample description")
                    .hostUsername("user")
                    .build(),
                securityUtils,
                spaceService));
      }
    }

    return grid;
  }
}
