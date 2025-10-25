package com.shikshaspace.shikshaspaceui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "shikshaspace")
public class ShikshaspaceUiApplication implements AppShellConfigurator {

  public static void main(String[] args) {
    SpringApplication.run(ShikshaspaceUiApplication.class, args);
  }
}
