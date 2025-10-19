package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * Home page - default landing page after login
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | ShikshaSpace")
@PermitAll
public class HomePage extends VerticalLayout {

    public HomePage() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 welcome = new H2("Welcome to ShikshaSpace!");
        welcome.getStyle()
                .set("color", "#667eea")
                .set("margin-bottom", "10px");

        Paragraph message = new Paragraph(
                "This is your home page. ShikshaSpace content will appear here after integration."
        );
        message.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("text-align", "center")
                .set("max-width", "600px");

        add(welcome, message);
    }
}
