# Android Flicker

An Android application built with Kotlin, Jetpack Compose, Hilt, Retrofit, and Navigation Compose.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| DI | Hilt |
| Networking | Retrofit + OkHttp |
| Navigation | Navigation Compose |
| Async | Coroutines |
| Architecture | MVVM |

---

## Environments

The project uses three product flavors:

| Flavor | Application ID | API |
|---|---|---|
| `dev` | `com.androidflicker.flickergallery.dev` | `https://dev-api.flickr.com/` |
| `staging` | `com.androidflicker.flickergallery.staging` | `https://staging-api.flickr.com/` |
| `production` | `com.androidflicker.flickergallery` | `https://api.flickr.com/` |

Build a specific variant:
```bash
./gradlew assembleDevDebug
./gradlew assembleStagingDebug
./gradlew assembleProductionRelease
```

---

## Branch Strategy

| Branch | Purpose |
|---|---|
| `main` | Production-ready code only |
| `develop` | Integration branch — all features merge here first |
| `feature/AND-*` | Feature branches created from `develop` |

### Rules
- **No direct pushes** to `main` or `develop`
- All changes must go through a **Pull Request**
- PRs require **1 approving review** before merge
- PRs require **all CI checks to pass** before merge
- New commits on a PR **dismiss previous approvals**

---

## CI Pipeline

GitHub Actions runs five checks automatically on every PR and push to `main`/`develop`:

| Job | Command | What it checks |
|---|---|---|
| Build | `./gradlew assembleDevDebug` | Project compiles |
| Unit Tests & Coverage | `./gradlew testDevDebugUnitTest jacocoDevDebugReport jacocoDevDebugCoverageVerification` | Tests pass + ≥70% coverage |
| Lint | `./gradlew lintDevDebug` | Android lint rules |
| Static Analysis | `./gradlew detekt` | Code quality (Detekt) |
| Formatting | `./gradlew spotlessCheck` | Code style (Spotless + ktlint) |

### Run checks locally before pushing

```bash
# Full build
./gradlew assembleDevDebug

# Unit tests only
./gradlew testDevDebugUnitTest

# Generate coverage report
./gradlew jacocoDevDebugReport

# Verify 70% coverage threshold
./gradlew jacocoDevDebugCoverageVerification

# Lint
./gradlew lintDevDebug

# Static analysis
./gradlew detekt

# Formatting check
./gradlew spotlessCheck

# Auto-fix formatting
./gradlew spotlessApply
```

---

## Unit Test Coverage (JaCoCo)

JaCoCo enforces a **minimum 70% instruction coverage** gate. PRs are blocked when coverage drops below the threshold.

### Run locally

```bash
# Run tests, generate report, and verify threshold in one go
./gradlew testDevDebugUnitTest jacocoDevDebugReport jacocoDevDebugCoverageVerification

# Individual steps
./gradlew testDevDebugUnitTest                   # run unit tests
./gradlew jacocoDevDebugReport                   # generate HTML + XML report
./gradlew jacocoDevDebugCoverageVerification     # fail if below 70%
```

### Reports

```
app/build/reports/jacoco/devDebug/
├── html/index.html      ← open in browser for line-by-line coverage
└── jacocoDevDebugReport.xml  ← consumed by CI
```

### Coverage threshold

| Metric | Threshold |
|---|---|
| Instruction coverage | 70% |

To change the threshold, update `minimum` in `jacocoDevDebugCoverageVerification` inside `app/build.gradle.kts`.

### What is excluded

The following are excluded from coverage calculation — they contain no testable business logic:

- Android generated classes (`R`, `BuildConfig`, `Manifest`)
- Hilt / Dagger generated classes (`*Hilt_*`, `*_Factory`, `*_MembersInjector`, `Dagger*`)
- Compose generated classes (`*ComposableSingletons*`)
- Theme, Navigation, and Preview code
- Android entry points (`MainActivity`, `FlickerGalleryApp`)
- Interfaces with no implementation (`FlickerRepository`, `ApiService`)

**Do not add real business logic to the exclusion list to inflate coverage numbers.**

---

## Static Analysis (Detekt)

Detekt is the Kotlin static analysis tool used in this project. It enforces code quality rules and **blocks PRs on violations**.

### What Detekt checks

| Category | Examples |
|---|---|
| Unused code | Unused imports, unused private members, dead code |
| Complexity | Cyclomatic complexity, cognitive complexity, nested blocks |
| Long methods | Method length, parameter count |
| Magic numbers | Unexplained hardcoded numeric values |
| Unsafe practices | Unsafe casts, swallowed exceptions, empty catch blocks |
| Coroutines | `GlobalScope` usage, redundant suspend modifiers |
| Naming | Classes, functions, variables, packages |
| Style | Line length, wildcard imports, return count |

### Run locally

```bash
# Run full static analysis
./gradlew detekt

# Generate baseline for pre-existing violations (use sparingly)
./gradlew detektBaseline
```

### Reports

After running `./gradlew detekt`, reports are written to:

```
build/reports/detekt/
├── detekt.html    ← human-readable, open in browser
├── detekt.xml     ← machine-readable, for CI tools
└── detekt.sarif   ← GitHub Security tab integration
```

### Fix violations

Detekt error output identifies the file, line, and rule. Example:

```
ComplexMethod - [onCreate] at MainActivity.kt:16:5
```

Open the file at the indicated line, simplify the code, and re-run `./gradlew detekt` to confirm the fix.

### Baseline strategy

A baseline (`detekt-baseline.xml`) records pre-existing violations so they are excluded from new analysis.

**Rules:**
- The baseline should only cover violations that existed before Detekt was introduced
- New code must pass all rules — do not add new violations to the baseline
- Shrink the baseline over time by fixing legacy issues

To regenerate the baseline:

```bash
./gradlew detektBaseline
```

### Configuration

Rules are configured in `config/detekt/detekt.yml`. Key decisions:

- `@Composable` functions are exempt from `LongMethod` and `LongParameterList`
- `@Preview` functions are exempt from `UnusedPrivateMember`
- `MagicNumber` allows `-1`, `0`, `1`, `2`, named arguments, and constants
- `WildcardImport` allows Compose API namespaces

---

## Pull Request Workflow

1. Branch off `develop`:
   ```bash
   git checkout develop && git pull origin develop
   git checkout -b feature/AND-<ticket>-<short-description>
   ```
2. Make changes and run checks locally
3. Push and open a PR targeting `develop`
4. CI runs automatically — all 5 checks must pass
5. At least 1 reviewer must approve
6. Merge via GitHub UI (squash or merge commit)

---

## Architecture

This project follows **Clean MVVM Architecture** with three distinct layers.

### Dependency direction

```
Presentation  →  Domain  ←  Data
```

- **Presentation** depends on **Domain** (uses use cases, domain models)
- **Data** depends on **Domain** (implements domain interfaces)
- **Domain** depends on **nothing** — pure Kotlin

### Layers

**Presentation** (`presentation/`)
- Compose screens render `UiState` and emit `UiEvent` to ViewModels
- ViewModels call use cases and map results to `UiState`
- No Retrofit calls, no DTOs, no business logic in UI

**Domain** (`domain/`)
- `model/` — pure Kotlin data classes, no framework dependencies
- `repository/` — interfaces (contracts) defining data operations
- `usecase/` — single-responsibility business logic units

**Data** (`data/`)
- `remote/api/` — Retrofit `ApiService` interface
- `remote/dto/` — JSON response shapes (never exposed to domain/presentation)
- `remote/mapper/` — converts DTOs → domain models
- `remote/datasource/` — wraps API calls, returns DTOs internally
- `repository/` — implements domain interfaces, handles exceptions, returns `AppResult`

**Core** (`core/`)
- `result/AppResult` — `Success<T>` / `Error` wrapper for all async operations
- `error/AppError` — typed errors: `Network`, `Server`, `Unknown`
- `config/` — environment configuration
- `network/` — network constants

### Data flow (example: Home screen)

```
HomeScreen
  → HomeViewModel.onEvent(LoadPhotos)
    → GetPhotosUseCase.invoke()
      → PhotoRepository.getRecentPhotos()         ← domain interface
        → PhotoRepositoryImpl                      ← data implementation
          → PhotoRemoteDataSource.getRecentPhotos()
            → ApiService.getRecentPhotos()         ← Retrofit
              ← FlickrResponse (DTO)
            ← List<PhotoDto>
          ← List<PhotoDto>
        → PhotoMapper.toDomain()
        ← AppResult<List<Photo>>                   ← domain model
      ← AppResult<List<Photo>>
    ← AppResult<List<Photo>>
  → _uiState.update { HomeUiState(photos = ...) }
HomeScreen re-renders from new UiState
```

### Rules for new features

1. Add domain model in `domain/model/`
2. Add repository interface in `domain/repository/`
3. Add use case in `domain/usecase/`
4. Add DTO in `data/remote/dto/`, mapper in `data/remote/mapper/`
5. Add data source in `data/remote/datasource/`
6. Implement repository in `data/repository/`
7. Bind in `di/RepositoryModule`
8. Add `UiState` + `Event` in `presentation/screen/<name>/`
9. ViewModel calls use case only — never repository or API directly
10. Screen renders state only — never calls use cases directly

---

## Project Structure

```
app/src/main/java/com/androidflicker/flickergallery/
├── core/
│   ├── config/          # Environment, EnvironmentConfig
│   ├── error/           # AppError sealed class
│   ├── network/         # NetworkConstants
│   └── result/          # AppResult sealed class
├── data/
│   ├── remote/
│   │   ├── api/         # ApiService (Retrofit interface)
│   │   ├── datasource/  # PhotoRemoteDataSource
│   │   ├── dto/         # PhotoDto, FlickrResponse (never leave data layer)
│   │   └── mapper/      # DTO → domain model mappers
│   └── repository/      # PhotoRepositoryImpl
├── di/                  # NetworkModule, RepositoryModule
├── domain/
│   ├── model/           # Photo (pure Kotlin domain model)
│   ├── repository/      # PhotoRepository interface
│   └── usecase/         # GetPhotosUseCase
└── presentation/
    ├── navigation/      # AppNavHost, NavRoutes
    ├── screen/home/     # HomeScreen, HomeViewModel, HomeUiState, HomeEvent
    └── theme/           # Color, Theme, Type
```

---

## Dependency Management

All dependency versions are centralized in `gradle/libs.versions.toml`.
To update a dependency, change its version value there — no other file needs to change.
Run `./gradlew :app:lintDebug` after updating; lint will flag further outdated versions.
