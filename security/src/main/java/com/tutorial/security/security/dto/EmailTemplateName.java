package com.tutorial.security.security.dto;

public enum EmailTemplateName {
    ACTIVATE("activate_account"); //spring & thymeleaf auto scans for templates in resources/templates folder

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}
}
