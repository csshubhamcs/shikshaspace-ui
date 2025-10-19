package com.shikshaspace.shikshaspace_ui.components;

import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * Professional profile edit dialog with modern UI
 */
public class ProfileEditDialog extends Dialog {

    @Getter
    private final TextField firstNameField;
    @Getter
    private final TextField lastNameField;
    @Getter
    private final EmailField emailField;
    @Getter
    private final IntegerField ageField;
    @Getter
    private final TextField experienceField;
    @Getter
    private final TextArea bioField;
    @Getter
    private final TextField linkedinField;
    @Getter
    private final TextField githubField;
    @Getter
    private final TextField profileImageField;

    private final Button saveButton;
    private final Button cancelButton;

    public ProfileEditDialog(UserResponse user, Consumer<UserResponse> onSave) {
        // Dialog configuration
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);  // ← Click outside to close
        setWidth("600px");
        setModal(true);

        // Header
        H3 title = new H3("Edit Profile");
        title.getStyle()
                .set("margin", "0")
                .set("color", "#667eea");

        // Form fields
        firstNameField = new TextField("First Name");
        firstNameField.setValue(user.getFirstName() != null ? user.getFirstName() : "");
        firstNameField.setRequired(true);

        lastNameField = new TextField("Last Name");
        lastNameField.setValue(user.getLastName() != null ? user.getLastName() : "");
        lastNameField.setRequired(true);

        emailField = new EmailField("Email");
        emailField.setValue(user.getEmail() != null ? user.getEmail() : "");
        emailField.setReadOnly(true); // Email can't be changed
        emailField.setHelperText("Email cannot be changed");

        ageField = new IntegerField("Age");
        ageField.setValue(user.getAge() != null ? user.getAge() : 0);
        ageField.setMin(0);
        ageField.setMax(150);

        experienceField = new TextField("Experience (years)");
        experienceField.setValue(user.getExperience() != null ? String.valueOf(user.getExperience()) : "");
        experienceField.setPattern("[0-9]+(\\.[0-9]+)?");
        experienceField.setHelperText("e.g., 5 or 5.5");

        bioField = new TextArea("Bio");
        bioField.setValue(user.getBio() != null ? user.getBio() : "");
        bioField.setMaxLength(500);
        bioField.setHelperText(bioField.getValue().length() + "/500");
        bioField.addValueChangeListener(e -> 
            bioField.setHelperText(e.getValue().length() + "/500"));

        linkedinField = new TextField("LinkedIn URL");
        linkedinField.setValue(user.getLinkedinUrl() != null ? user.getLinkedinUrl() : "");
        linkedinField.setPlaceholder("https://linkedin.com/in/username");

        githubField = new TextField("GitHub URL");
        githubField.setValue(user.getGithubUrl() != null ? user.getGithubUrl() : "");
        githubField.setPlaceholder("https://github.com/username");

        profileImageField = new TextField("Profile Image URL");
        profileImageField.setValue(user.getProfileImageUrl() != null ? user.getProfileImageUrl() : "");
        profileImageField.setPlaceholder("https://example.com/image.jpg");

        // Form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstNameField, lastNameField,
                emailField, ageField,
                experienceField, bioField,
                linkedinField, githubField,
                profileImageField
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setColspan(bioField, 2);
        formLayout.setColspan(linkedinField, 2);
        formLayout.setColspan(githubField, 2);
        formLayout.setColspan(profileImageField, 2);

        // Buttons
        saveButton = new Button("Save Changes", e -> {
            if (validateFields()) {
                // Build updated user object
                UserResponse updated = new UserResponse();
                updated.setId(user.getId());
                updated.setUsername(user.getUsername());
                updated.setFirstName(firstNameField.getValue());
                updated.setLastName(lastNameField.getValue());
                updated.setEmail(emailField.getValue());
                updated.setAge(ageField.getValue());
                
                try {
                    updated.setExperience(Double.parseDouble(experienceField.getValue()));
                } catch (NumberFormatException ex) {
                    updated.setExperience(null);
                }
                
                updated.setBio(bioField.getValue());
                updated.setLinkedinUrl(linkedinField.getValue());
                updated.setGithubUrl(githubField.getValue());
                updated.setProfileImageUrl(profileImageField.getValue());

                onSave.accept(updated);
                close();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton = new Button("Cancel", e -> close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        buttons.getStyle().set("margin-top", "20px");

        // Add to dialog
        add(title, formLayout, buttons);
    }

    private boolean validateFields() {
        if (firstNameField.getValue().isBlank() || lastNameField.getValue().isBlank()) {
            Notification.show("First Name and Last Name are required", 
                    3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }
}
