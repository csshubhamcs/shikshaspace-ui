package com.shikshaspace.shikshaspaceui.views.home;

import com.shikshaspace.shikshaspaceui.component.NavBar;
import com.shikshaspace.shikshaspaceui.component.TopicCard;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomePage extends VerticalLayout {

  public HomePage() {
    setPadding(false);
    setSpacing(false);
    setSizeFull();

    // Navigation bar
    NavBar navBar = new NavBar();

    // Main content wrapper
    Div contentWrapper = new Div();
    contentWrapper.addClassName("home-content");
    contentWrapper
        .getStyle()
        .set("padding", "40px 60px")
        .set("max-width", "1400px")
        .set("margin", "0 auto")
        .set("width", "100%");

    // Welcome header
    H1 welcomeTitle = new H1("Welcome to ShikshaSpace");
    welcomeTitle.addClassName("welcome-title");
    welcomeTitle.getStyle().set("text-align", "center").set("margin", "0 0 32px 0");

    // User info card
    Div userInfoCard = createUserInfoCard();

    // Topics grid with FORCED grid layout
    Div topicsGrid = createTopicsGrid();

    contentWrapper.add(welcomeTitle, userInfoCard, topicsGrid);

    add(navBar, contentWrapper);
  }

  private Div createUserInfoCard() {
    Div card = new Div();
    card.addClassName("user-info-card");
    card.getStyle()
        .set("background", "linear-gradient(135deg, #ffffff 0%, #f8f9ff 100%)")
        .set("border", "2px solid #667eea")
        .set("border-radius", "12px")
        .set("padding", "14px 28px")
        .set("margin", "0 0 40px auto")
        .set("max-width", "280px")
        .set("text-align", "center")
        .set("font-size", "15px")
        .set("font-weight", "600")
        .set("color", "#667eea");

    Span text = new Span(" Create ShikshaSpace");
    card.add(text);

    return card;
  }

  private Div createTopicsGrid() {
    Div grid = new Div();
    grid.addClassName("topics-grid");

    // FORCE grid layout with inline styles
    grid.getStyle()
        .set("display", "grid")
        .set("grid-template-columns", "repeat(3, 1fr)")
        .set("gap", "24px")
        .set("margin-top", "32px")
        .set("width", "100%");

    // Dummy data
    grid.add(
        new TopicCard(
            "Agentic AI",
            "Agentic AI build with Rag + implementation",
            "rahul(user details)",
            "today 8:00 PM"),
        new TopicCard(
            "Agentic AI",
            "Agentic AI build with Rag + implementation",
            "rahul(user details)",
            "today 8:00 PM"),
        new TopicCard(
            "Agentic AI",
            "Agentic AI build with Rag + implementation",
            "rahul(user details)",
            "today 8:00 PM"),
        new TopicCard(
            "Machine Learning",
            "ML fundamentals and advanced concepts",
            "priya(user details)",
            "tomorrow 6:00 PM"),
        new TopicCard(
            "Web Development",
            "Full-stack development with Spring Boot",
            "amit(user details)",
            "today 7:00 PM"),
        new TopicCard(
            "Data Science",
            "Python for data analysis and visualization",
            "neha(user details)",
            "tomorrow 5:00 PM"));

    return grid;
  }
}
