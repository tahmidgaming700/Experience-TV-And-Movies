# Experience TV

**Entertainment without boundaries.**

Experience TV is an independent streaming-platform project with two separate native Android applications.

- **Experience TV Mobile** — `com.experiencetv.mobile`, Android 9 / API 28+
- **Experience TV for Android TV** — `com.experiencetv.androidtv`, Android TV 9 / API 28+
- **Shared core** — catalogue models used by both clients.

## Current build: 0.1.0 foundation

The repository now contains:
- A Gradle Kotlin DSL multi-module Android project.
- A native Jetpack Compose mobile browsing app.
- A remote-first Android TV browsing app with focusable D-pad controls.
- Shared sample catalogue data.
- Home, Discover, Search, title details, and local My List interactions.
- Independent GitHub Actions workflows that build and upload debug APKs.

**Current limitations:** the catalogue is sample data, My List is local in-memory demo state, artwork uses remote sample images, and the Play action is a placeholder. There is not yet a production backend, account system, cross-device sync, licensed video stream, payment system, offline download system, or DRM service. These are subsequent implementation milestones, not features to assume are already operational.

## Build locally

Requirements: JDK 17 and Gradle 8.11.1.

```bash
gradle :apps:mobile:app:assembleDebug
gradle :apps:android-tv:app:assembleDebug
```

APK outputs:
- `apps/mobile/app/build/outputs/apk/debug/`
- `apps/android-tv/app/build/outputs/apk/debug/`

GitHub Actions also builds each application independently on relevant pushes, pull requests, or manual workflow dispatch.

## Architecture

- Android clients: Kotlin, Jetpack Compose; Android TV uses focusable remote navigation.
- Backend (planned): Kotlin/JVM with Spring Boot, initially a modular monolith.
- Data (planned): PostgreSQL and versioned migrations.
- Media (planned): provider-neutral adapter, with Jellyfin as a development integration.
- Playback (planned): AndroidX Media3 and permitted HLS/DASH streams.
- Studio (planned): content ingestion, metadata, subtitles, rights windows, publishing, and audit tools.

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) and [docs/LICENSING.md](docs/LICENSING.md).

## Content and rights

Only stream content for which the project has documented distribution rights or a verified public-domain/open licence applicable to the viewer's territory. The backend must enforce rights windows and entitlements. A media-server library is not proof of commercial streaming rights.

## Security

Never commit credentials, OAuth client secrets, signing keys, private certificates, or production tokens. Google identity tokens must be verified by the backend; never trust a client-supplied user ID.

## Licence

A repository-wide open-source licence will be selected after the code and dependency licensing strategy is reviewed. Third-party components retain their own licences.
