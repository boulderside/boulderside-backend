# Repository Guidelines

## Project Structure & Module Organization
Source lives under `src/main/java/com/line7studio/boulderside`, grouped by aggregate (e.g., `domain/aggregate/boulder`) with `entity`, `service`, and `repository` folders per domain. `src/main/resources` hosts `application-*.yml`, static assets, and Flyway/sql scripts. Integration fixtures, REST docs, and other assets should sit beside the feature they validate. Generated QueryDSL sources land in `build/generated/querydsl`; never edit them manually. Tests mirror the main tree in `src/test/java`, while environment samples are versioned as `boulderside-backend.env`.

## Build, Test, and Development Commands
- `./gradlew bootRun` – launches the Spring Boot API with the active profile (`SPRING_PROFILES_ACTIVE=local` for local dev).
- `./gradlew build` – performs compilation, QueryDSL generation, static analysis, and unit/integration tests before producing the runnable jar under `build/libs/`.
- `./gradlew test` – runs the JUnit Platform suites; add `--tests "*BoulderService*"` to focus on a class.
- `./gradlew clean` – removes `build/`, including generated QueryDSL sources; rerun build afterward to regenerate Q-types.

## Coding Style & Naming Conventions
Java 21 with Spring Boot 3.4.7 is enforced via the Gradle toolchain. Use 4-space indentation, Lombok for DTOs/entities when appropriate, and annotate transactional boundaries at the service layer. Package names stay lowercase and aligned with the aggregate structure. QueryDSL Q-classes keep the default `Q<Entity>` naming. Prefer descriptive method names such as `findPublishedBoulders()` and align REST controllers under `com.line7studio.boulderside.api.<resource>`.

## Testing Guidelines
JUnit 5 with Spring Boot Starter Test is the default stack; MockMvc powers API tests and `@DataJpaTest` covers repositories. Follow the `{ClassName}Test` / `{ClassName}IntegrationTest` naming. Each non-trivial service or repository change requires at least one regression test plus happy-path/edge coverage, targeting ~80% coverage on touched modules. Run `./gradlew test` before pushing; CI mirrors this command.

## Commit & Pull Request Guidelines
Existing history uses prefixed messages (`feat:`, `fix:`, `refactor:`) optionally followed by a short Korean or English summary and issue reference (`#62`). Keep subject lines under 72 characters and describe the motivation in the body when needed. PRs against `develop` should include: purpose summary, testing evidence (command and result), linked GitHub issues, and screenshots or API contract diffs when UX or Swagger changes occur. Request reviews from at least one domain owner and wait for CI to pass before merging.

## Security & Configuration Tips
Secrets (DB, AWS, OAuth) must be injected via environment variables or externalized config such as `application-local.yml`; do not commit real credentials. Use `boulderside-backend.env` as the template, sourcing it via `export $(cat boulderside-backend.env | xargs)` before running the app locally. Validate new endpoints behind Spring Security by covering both authenticated and anonymous flows, and document required scopes in the PR.
