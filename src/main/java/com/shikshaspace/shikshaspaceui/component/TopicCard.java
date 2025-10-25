package com.shikshaspace.shikshaspaceui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class TopicCard extends Div {

  public TopicCard(String title, String subtitle, String author, String time) {
    addClassName("topic-card");

    // Force card styling
    getStyle()
        .set("background", "white")
        .set("border", "2px solid #667eea")
        .set("border-radius", "16px")
        .set("padding", "24px")
        .set("display", "flex")
        .set("flex-direction", "column")
        .set("gap", "12px")
        .set("min-height", "220px")
        .set("transition", "all 0.3s ease")
        .set("cursor", "pointer");

    // Title
    Span titleSpan = new Span(title);
    titleSpan.addClassName("topic-title");
    titleSpan
        .getStyle()
        .set("font-size", "20px")
        .set("font-weight", "600")
        .set("color", "#1a1a1a")
        .set("display", "block")
        .set("margin-bottom", "8px");

    // Subtitle
    Span subtitleSpan = new Span(subtitle);
    subtitleSpan.addClassName("topic-subtitle");
    subtitleSpan
        .getStyle()
        .set("font-size", "14px")
        .set("color", "#666")
        .set("line-height", "1.5")
        .set("display", "block")
        .set("margin-bottom", "12px")
        .set("flex", "1"); // Push button to bottom

    // Author
    Span authorSpan = new Span(author);
    authorSpan.addClassName("topic-author");
    authorSpan
        .getStyle()
        .set("font-size", "13px")
        .set("color", "#667eea")
        .set("font-weight", "500")
        .set("display", "block")
        .set("margin-bottom", "4px");

    // Time
    Span timeSpan = new Span(time);
    timeSpan.addClassName("topic-time");
    timeSpan
        .getStyle()
        .set("font-size", "13px")
        .set("color", "#999")
        .set("display", "block")
        .set("margin-bottom", "12px");

    // Join button
    Button joinButton = new Button("join");
    joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    joinButton.addClassName("topic-join-btn");
    joinButton.addClickListener(
        e -> {
          joinButton.setText("Joined!");
        });

    add(titleSpan, subtitleSpan, authorSpan, timeSpan, joinButton);

    // Hover effect
    getElement()
        .addEventListener(
            "mouseenter",
            e -> {
              getStyle()
                  .set("transform", "translateY(-6px)")
                  .set("box-shadow", "0 12px 32px rgba(102, 126, 234, 0.25)");
            });

    getElement()
        .addEventListener(
            "mouseleave",
            e -> {
              getStyle().set("transform", "translateY(0)").set("box-shadow", "none");
            });
  }
}
