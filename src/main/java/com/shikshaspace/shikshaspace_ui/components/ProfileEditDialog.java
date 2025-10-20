package com.shikshaspace.shikshaspace_ui.components;

import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.Consumer;
import lombok.Getter;

/**
 * Production-grade responsive ProfileEditDialog with modern premium design Supports mobile
 * (320px+), tablet (768px+), and desktop (1024px+)
 *
 * <p>Features: - Mobile-first responsive design - Modern glassmorphism effects - Smooth animations
 * - Touch-friendly form fields - Fluid typography - Premium button styling
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
public class ProfileEditDialog extends Dialog {

  @Getter private final TextField firstNameField;
  @Getter private final TextField lastNameField;
  @Getter private final EmailField emailField;
  @Getter private final IntegerField ageField;
  @Getter private final TextField experienceField;
  @Getter private final TextArea bioField;
  @Getter private final TextField linkedinField;
  @Getter private final TextField githubField;
  @Getter private final TextField profileImageField;

  private Button saveButton;
  private Button cancelButton;

  public ProfileEditDialog(UserResponse user, Consumer<UserResponse> onSave) {
    // Dialog configuration with responsive width
    setCloseOnEsc(true);
    setCloseOnOutsideClick(true);
    setModal(true);
    addClassName("profile-edit-dialog");

    // Responsive dialog width
    setWidth("clamp(320px, 90vw, 650px)");
    setMaxHeight("90vh");

    // Apply modern styling
    getElement()
        .getStyle()
        .set("border-radius", "clamp(12px, 2vw, 20px)")
        .set("box-shadow", "0 12px 40px rgba(102, 126, 234, 0.2)");

    // Create dialog content
    Div dialogContent = new Div();
    dialogContent.addClassName("dialog-content-wrapper");
    dialogContent
        .getStyle()
        .set("padding", "clamp(20px, 4vw, 32px)")
        .set("max-height", "85vh")
        .set("overflow-y", "auto");

    // Header with gradient title and close button
    HorizontalLayout header = createHeader();

    // Form fields
    firstNameField = createTextField("First Name", user.getFirstName(), true);
    lastNameField = createTextField("Last Name", user.getLastName(), true);

    emailField = createEmailField(user.getEmail());

    ageField = createAgeField(user.getAge());

    experienceField = createExperienceField(user.getExperience());

    bioField = createBioField(user.getBio());

    linkedinField =
        createUrlField("LinkedIn URL", user.getLinkedinUrl(), "https://linkedin.com/in/username");

    githubField = createUrlField("GitHub URL", user.getGithubUrl(), "https://github.com/username");

    profileImageField =
        createUrlField(
            "Profile Image URL", user.getProfileImageUrl(), "https://example.com/image.jpg");

    // Form layout with responsive columns
    FormLayout formLayout = new FormLayout();
    formLayout.addClassName("profile-form-layout");
    formLayout.add(
        firstNameField,
        lastNameField,
        emailField,
        ageField,
        experienceField,
        bioField,
        linkedinField,
        githubField,
        profileImageField);

    // Responsive form steps
    formLayout.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1), // Mobile: 1 column
        new FormLayout.ResponsiveStep("500px", 2) // Desktop: 2 columns
        );

    // Full-width for specific fields
    formLayout.setColspan(bioField, 2);
    formLayout.setColspan(linkedinField, 2);
    formLayout.setColspan(githubField, 2);
    formLayout.setColspan(profileImageField, 2);

    // Action buttons with modern design
    HorizontalLayout buttons = createButtons(user, onSave);

    dialogContent.add(header, formLayout, buttons);
    add(dialogContent);
  }

  /** Create header with gradient title and close button */
  private HorizontalLayout createHeader() {
    HorizontalLayout header = new HorizontalLayout();
    header.setWidthFull();
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    header.addClassName("dialog-header");
    header
        .getStyle()
        .set("margin-bottom", "clamp(16px, 3vw, 24px)")
        .set("padding-bottom", "16px")
        .set("border-bottom", "2px solid rgba(102, 126, 234, 0.1)");

    // ✅ FIX: Title with SOLID DARK COLOR (not gradient)
    H3 title = new H3("Edit Profile");
    title.addClassName("dialog-title");
    title
        .getStyle()
        .set("margin", "0")
        .set("color", "#1a1a1a") // ✅ SOLID dark color (visible!)
        .set("font-size", "clamp(1.3rem, 4vw, 1.75rem)")
        .set("font-weight", "700");

    // Close button
    Button closeButton = new Button(VaadinIcon.CLOSE.create());
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    closeButton.addClassName("dialog-close-button");
    closeButton
        .getStyle()
        .set("cursor", "pointer")
        .set("border-radius", "50%")
        .set("transition", "all 0.3s ease");
    closeButton.addClickListener(e -> close());

    header.add(title, closeButton);
    return header;
  }

  /** Create standard text field with styling */
  private TextField createTextField(String label, String value, boolean required) {
    TextField field = new TextField(label);
    field.setValue(value != null ? value : "");
    field.setRequired(required);
    field.setClearButtonVisible(true);
    styleInputField(field);
    return field;
  }

  /** Create email field */
  private EmailField createEmailField(String email) {
    EmailField field = new EmailField("Email");
    field.setValue(email != null ? email : "");
    field.setReadOnly(true);
    field.setHelperText("Email cannot be changed");
    styleInputField(field);

    // Add lock icon for readonly field
    field.setPrefixComponent(VaadinIcon.LOCK.create());

    return field;
  }

  /** Create age field */
  private IntegerField createAgeField(Integer age) {
    IntegerField field = new IntegerField("Age");
    field.setValue(age != null && age > 0 ? age : null);
    field.setMin(0);
    field.setMax(150);
    field.setHelperText("Optional");
    styleInputField(field);
    return field;
  }

  /** Create experience field */
  private TextField createExperienceField(Double experience) {
    TextField field = new TextField("Experience (years)");
    field.setValue(experience != null ? String.valueOf(experience) : "");
    field.setPattern("[0-9]+(\\.[0-9]+)?");
    field.setHelperText("e.g., 5 or 5.5 (optional)");
    styleInputField(field);
    return field;
  }

  /** Create bio field with character counter */
  private TextArea createBioField(String bio) {
    TextArea field = new TextArea("Bio");
    field.setValue(bio != null ? bio : "");
    field.setMaxLength(500);
    field.setHelperText(field.getValue().length() + "/500");

    // Update character count dynamically
    field.addValueChangeListener(e -> field.setHelperText(e.getValue().length() + "/500"));

    styleInputField(field);

    // Additional styling for textarea
    field.getStyle().set("min-height", "120px").set("resize", "vertical");

    return field;
  }

  /** Create URL field */
  private TextField createUrlField(String label, String value, String placeholder) {
    TextField field = new TextField(label);
    field.setValue(value != null ? value : "");
    field.setPlaceholder(placeholder);
    field.setClearButtonVisible(true);
    field.setHelperText("Optional");
    styleInputField(field);
    return field;
  }

  /** Apply consistent styling to input fields */
  private void styleInputField(com.vaadin.flow.component.Component field) {
    field
        .getElement()
        .getStyle()
        .set("--vaadin-input-field-border-radius", "10px")
        .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
        .set("--vaadin-input-field-border-width", "2px")
        .set("--vaadin-input-field-border-color", "rgba(102, 126, 234, 0.1)")
        .set("--vaadin-input-field-hover-highlight", "#667eea")
        .set("--vaadin-input-field-focus-ring", "0 0 0 3px rgba(102, 126, 234, 0.15)")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("transition", "all 0.3s ease");

    field.getElement().getClassList().add("premium-dialog-input");
  }

  /** Create action buttons */
  private HorizontalLayout createButtons(UserResponse user, Consumer<UserResponse> onSave) {
    // Save button with premium styling
    saveButton = new Button("Save Changes");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.setIcon(VaadinIcon.CHECK.create());
    saveButton.addClassName("premium-save-button");

    saveButton
        .getStyle()
        .set("border-radius", "10px")
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("font-weight", "600")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("padding", "clamp(12px, 2.5vw, 14px) clamp(20px, 4vw, 28px)")
        .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.3)")
        .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
        .set("cursor", "pointer");

    // Hover effects
    saveButton
        .getElement()
        .addEventListener(
            "mouseenter",
            e -> {
              saveButton
                  .getStyle()
                  .set("transform", "translateY(-2px)")
                  .set("box-shadow", "0 6px 16px rgba(102, 126, 234, 0.4)");
            });

    saveButton
        .getElement()
        .addEventListener(
            "mouseleave",
            e -> {
              saveButton
                  .getStyle()
                  .set("transform", "translateY(0)")
                  .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.3)");
            });

    saveButton.addClickListener(
        e -> {
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
              updated.setExperience(
                  experienceField.getValue() != null && !experienceField.getValue().isEmpty()
                      ? Double.parseDouble(experienceField.getValue())
                      : null);
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

    // Cancel button with modern styling
    cancelButton = new Button("Cancel");
    cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    cancelButton.addClassName("premium-cancel-button");

    cancelButton
        .getStyle()
        .set("border-radius", "10px")
        .set("font-weight", "600")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("padding", "clamp(12px, 2.5vw, 14px) clamp(20px, 4vw, 28px)")
        .set("transition", "all 0.3s ease")
        .set("cursor", "pointer")
        .set("border", "2px solid rgba(102, 126, 234, 0.2)");

    cancelButton.addClickListener(e -> close());

    // Button layout
    HorizontalLayout buttons = new HorizontalLayout(cancelButton, saveButton);
    buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    buttons.setWidthFull();
    buttons.addClassName("dialog-buttons");
    buttons
        .getStyle()
        .set("margin-top", "clamp(20px, 4vw, 28px)")
        .set("padding-top", "20px")
        .set("border-top", "2px solid rgba(102, 126, 234, 0.1)")
        .set("gap", "clamp(12px, 2vw, 16px)")
        .set("flex-wrap", "wrap");

    // On mobile, stack buttons
    buttons
        .getElement()
        .executeJs("if (window.innerWidth < 500) { this.style.flexDirection = 'column-reverse'; }");

    return buttons;
  }

  /** Validate form fields */
  private boolean validateFields() {
    if (firstNameField.getValue().isBlank() || lastNameField.getValue().isBlank()) {
      Notification notification =
          Notification.show(
              "First Name and Last Name are required", 4000, Notification.Position.TOP_CENTER);
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

      // Shake animation for error
      getElement()
          .executeJs(
              "this.style.animation = 'shake 0.5s'; "
                  + "setTimeout(() => this.style.animation = '', 500);");

      return false;
    }
    return true;
  }
}
