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
| Unit Tests | `./gradlew testDevDebugUnitTest` | Unit test suite |
| Lint | `./gradlew lintDevDebug` | Android lint rules |
| Static Analysis | `./gradlew detekt` | Code quality (Detekt) |
| Formatting | `./gradlew spotlessCheck` | Code style (Spotless + ktlint) |

### Run checks locally before pushing

```bash
# Full build
./gradlew assembleDevDebug

# Unit tests
./gradlew testDevDebugUnitTest

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

## Project Structure

```
app/src/main/java/com/androidflicker/flickergallery/
├── core/
│   ├── config/          # EnvironmentConfig, Environment enum
│   └── network/         # NetworkConstants
├── data/
│   ├── remote/api/      # ApiService (Retrofit)
│   └── repository/      # Repository implementations
├── di/                  # Hilt modules (AppModule, NetworkModule)
├── domain/
│   └── repository/      # Repository interfaces
└── presentation/
    ├── navigation/      # AppNavHost, NavRoutes
    ├── screen/home/     # HomeScreen, HomeViewModel
    └── theme/           # Color, Theme, Type
```

---

## Dependency Management

All dependency versions are centralized in `gradle/libs.versions.toml`.
To update a dependency, change its version value there — no other file needs to change.
Run `./gradlew :app:lintDebug` after updating; lint will flag further outdated versions.
