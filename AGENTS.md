# Repository Guidelines

## Project Structure & Module Organization

This repository is a Spring Boot 3.5 / Java 17 draft for presenting the LXP MVP screen flow. It is not intended to implement business features yet. Use Spring MVC controllers and Thymeleaf templates to show pages and basic navigation only.

- `src/main/java/wlsh/study/lxpfronttemplate`: Spring Boot application and MVC controllers.
- `src/main/resources/templates`: Thymeleaf views to add for each screen.
- `src/main/resources/static`: CSS, JavaScript, and image assets used by the views.
- `src/test/java/wlsh/study/lxpfronttemplate`: JUnit/Spring Boot tests.
- `docs/ui`: reference screen images, such as login, course list, cart, enrollment, and profile pages.
- `docs/PRD.md` and `docs/UI_GUIDE.md`: planning and UI guidance documents.

## Build, Test, and Development Commands

- `./gradlew bootRun`: start the local Spring Boot app.
- `./gradlew test`: run JUnit Platform tests.
- `./gradlew build`: compile, test, and package the project.
- `./gradlew clean`: remove generated build output.

Prefer the included Gradle wrapper instead of a system Gradle installation.

## Coding Style & Naming Conventions

Use Java 17 and standard Spring conventions. Keep controllers thin and page-oriented, for example `CourseController`, `CartController`, and `ProfileController`. Controller methods should return Thymeleaf view names and populate `Model` with mock data directly.

Use package names under `wlsh.study.lxpfronttemplate`. Use `PascalCase` for Java classes, `camelCase` for methods and variables, and lowercase kebab-case for template names such as `course-list.html` or `course-detail.html`.

## Mock Data & Scope Rules

All data should be hardcoded dummy data inside controllers or small local helper methods. Do not add persistence, repositories, external APIs, login sessions, or authorization logic unless explicitly requested.

For pages that normally require authentication, assume the user is already authenticated. The goal is to demonstrate screen composition and movement between screens, not production behavior.

## Testing Guidelines

The project uses Spring Boot Test with JUnit 5. Keep tests lightweight. Add context or MVC route tests when adding controllers, especially to verify that expected views render and model attributes exist.

Name test classes after the target class or feature, for example `CourseControllerTests`. Run `./gradlew test` before submitting changes.

## Commit & Pull Request Guidelines

The current history only contains `first commit`, so no strict commit convention is established. Use short, imperative messages such as `Add course list mock screen` or `Wire cart navigation`.

Pull requests should include a concise summary, affected screens, screenshots when UI changes are visible, and any assumptions about mock data or navigation behavior.
