package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "explore", layout = MainLayout.class)
@PageTitle("Explore - ShikshaSpace")
@PermitAll
public class ExploreView extends VerticalLayout {

  public ExploreView() {
    setSizeFull();
    setPadding(true);
    setAlignItems(Alignment.CENTER);

    getStyle()
        .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
        .set("padding", "clamp(24px, 5vw, 60px)")
        .set("cursor", "default"); // ✅ Remove pointer cursor

    // Content container
    VerticalLayout content = new VerticalLayout();
    content.setMaxWidth("1200px");
    content.setPadding(true);
    content.setAlignItems(Alignment.CENTER);

    // ✅ FIXED: Title with solid color (visible & centered)
    H1 title = new H1("Explore Sessions");
    title
        .getStyle()
        .set("color", "#667eea") // ✅ Solid color
        .set("text-align", "center")
        .set("font-size", "clamp(2rem, 5vw, 3rem)")
        .set("margin", "0 0 16px 0")
        .set("font-weight", "700");

    // Description
    Paragraph description =
        new Paragraph(
            "Discover upcoming learning sessions hosted by our community. "
                + "Filter by topic, date, or host to find sessions that match your interests.");
    description
        .getStyle()
        .set("text-align", "center")
        .set("color", "#666")
        .set("font-size", "1.1rem")
        .set("line-height", "1.8")
        .set("max-width", "700px")
        .set("margin", "0 auto 48px");

    // Placeholder
    Div placeholder = createPlaceholder();

    content.add(title, description, placeholder);
    add(content);
  }

  private Div createPlaceholder() {
    Div placeholder = new Div();

    Icon icon = VaadinIcon.SEARCH.create();
    icon.getStyle()
        .set("width", "64px")
        .set("height", "64px")
        .set("color", "#667eea")
        .set("margin-bottom", "16px");

    Paragraph text = new Paragraph("🚀 Session listing feature coming soon!");
    text.getStyle().set("color", "#999").set("font-size", "1.1rem").set("margin", "0");

    placeholder.add(icon, text);
    placeholder
        .getStyle()
        .set("background", "white")
        .set("padding", "60px 40px")
        .set("border-radius", "12px")
        .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)")
        .set("text-align", "center")
        .set("max-width", "600px")
        .set("margin", "0 auto")
        .set("display", "flex")
        .set("flex-direction", "column")
        .set("align-items", "center")
        .set("cursor", "default");

    return placeholder;
  }
}
