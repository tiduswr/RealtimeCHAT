package com.tiduswr.rcauth.models;

public enum EmailTemplateType {
    WELCOME("email-welcome-template"),
    RECOVER_PASSWORD("email-recover-password-template");

    private final String templateName;

    private EmailTemplateType(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }
}
