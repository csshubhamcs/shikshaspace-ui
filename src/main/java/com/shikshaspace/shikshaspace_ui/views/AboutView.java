package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("About - ShikshaSpace")
@PermitAll
public class AboutView extends VerticalLayout {

  public AboutView() {
    setSizeFull();
    setPadding(true);
    setSpacing(true);
    setAlignItems(Alignment.CENTER);

    getStyle()
        .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
        .set("padding", "clamp(24px, 5vw, 60px)")
        .set("cursor", "default"); // ✅ Remove pointer cursor

    // Content container
    VerticalLayout content = new VerticalLayout();
    content.setMaxWidth("900px");
    content.setPadding(true);
    content.setAlignItems(Alignment.CENTER);

    // ✅ FIXED: Title with solid color (visible)
    H1 title = new H1("About ShikshaSpace");
    title
        .getStyle()
        .set("color", "#667eea") // ✅ Solid color instead of gradient
        .set("text-align", "center")
        .set("font-size", "clamp(2rem, 5vw, 3rem)")
        .set("margin", "0 0 16px 0")
        .set("font-weight", "700");

    // Subtitle
    H2 subtitle = new H2("Learn, Host, and Share Knowledge");
    subtitle
        .getStyle()
        .set("color", "#333")
        .set("text-align", "center")
        .set("font-weight", "400")
        .set("font-size", "1.5rem")
        .set("margin", "0 0 24px 0");

    // Description
    Paragraph description =
        new Paragraph(
            "ShikshaSpace is a modern learning platform where users can host and join educational sessions. "
                + "Whether you want to teach a skill, share your knowledge, or learn something new, "
                + "ShikshaSpace provides the space to connect with learners and educators worldwide.");
    description
        .getStyle()
        .set("text-align", "center")
        .set("color", "#666")
        .set("font-size", "1.1rem")
        .set("line-height", "1.8")
        .set("max-width", "700px")
        .set("margin", "0 auto 48px");

    // Feature cards
    Div featuresSection = createFeaturesSection();

    content.add(title, subtitle, description, featuresSection);
    add(content);
  }

  private Div createFeaturesSection() {
    Div section = new Div();
    section
        .getStyle()
        .set("display", "grid")
        .set("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
        .set("gap", "24px")
        .set("width", "100%")
        .set("cursor", "default");

    section.add(
        createFeatureCard(
            VaadinIcon.PRESENTATION,
            "Host Sessions",
            "Share your knowledge by hosting live learning sessions on any topic."),
        createFeatureCard(
            VaadinIcon.USERS,
            "Join Community",
            "Connect with learners and educators from around the world."),
        createFeatureCard(
            VaadinIcon.DIPLOMA,
            "Learn & Grow",
            "Participate in sessions and expand your skill set."));

    return section;
  }

  private Div createFeatureCard(VaadinIcon icon, String title, String description) {
    Div card = new Div();
    card.getStyle()
        .set("background", "white")
        .set("padding", "28px")
        .set("border-radius", "12px")
        .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)")
        .set("text-align", "center")
        .set("transition", "transform 0.3s ease")
        .set("cursor", "default");

    Icon iconComponent = icon.create();
    iconComponent
        .getStyle()
        .set("color", "#667eea")
        .set("width", "48px")
        .set("height", "48px")
        .set("margin-bottom", "16px");

    H2 titleElement = new H2(title);
    titleElement
        .getStyle()
        .set("color", "#333")
        .set("font-size", "1.25rem")
        .set("margin", "0 0 12px 0");

    Paragraph desc = new Paragraph(description);
    desc.getStyle()
        .set("color", "#666")
        .set("margin", "0")
        .set("font-size", "0.95rem")
        .set("line-height", "1.6");

    card.add(iconComponent, titleElement, desc);

    // Hover effect
    card.getElement()
        .executeJs(
            "this.addEventListener('mouseenter', () => this.style.transform = 'translateY(-8px)');"
                + "this.addEventListener('mouseleave', () => this.style.transform = 'translateY(0)');");

    return card;
  }
}
