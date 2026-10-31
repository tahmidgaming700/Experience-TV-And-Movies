# Experience TV

**Entertainment without boundaries.**

Experience TV is an independent streaming platform project with two separate native Android applications:

- **Experience TV Mobile** — package: `com.experiencetv.mobile`, Android 9 (API 28)+
- **Experience TV for Android TV** — package: `com.experiencetv.androidtv`, Android TV 9 (API 28)+

Both clients share a cloud backend, catalogue, unified account system, profiles, watchlists, playback history, and content-rights enforcement.

## Project status

Early development / architecture stage. This repository is being scaffolded; no production streaming catalogue, commercial content licences, DRM service, or production infrastructure is claimed to exist yet.

## Architecture

- **Android clients:** Kotlin, Jetpack Compose; Compose for TV and D-pad navigation on TV.
- **Backend:** Kotlin/JVM with Spring Boot, initially a modular monolith.
- **Data:** PostgreSQL; development may use Supabase.
- **Media:** Jellyfin behind a provider-neutral adapter for development and eligible self-hosted media.
- **Playback:** AndroidX Media3; HLS/DASH where supported by the source and device.
- **Studio:** Web dashboard for content ingestion, metadata, subtitles, rights windows, and publishing.
- **Delivery:** Persistent media storage and a CDN are required for production. Serverless functions are not video origins or transcoding workers.

## Content and rights

Only stream content for which the project has documented distribution rights or a verified public-domain/open licence applicable to the viewer's territory. Catalogue metadata must record territory, rights window, permitted uses, and relevant restrictions. Client-side checks are not sufficient; the backend must enforce entitlements and availability.

Jellyfin is an optional media-server integration, not the Experience TV brand or a source of automatic commercial film licences. Its code and trademarks remain subject to their own terms.

## First milestone: 0.1.0

1. Buildable, separately installable mobile and Android TV apps.
2. Unified Experience Account with Google sign-in.
3. Shared catalogue, profiles, search, and watchlist.
4. Playback of one verified open-licensed test title through the media adapter.
5. Watch progress synchronised between phone and TV.
6. Basic Studio dashboard and CI builds.

## Repository layout

```text
apps/
  mobile/
  android-tv/
backend/
  api/
  shared/
shared/
  api-contracts/
  design-tokens/
streaming/
  adapter/
  jellyfin/
studio/
  admin-web/
infrastructure/
  docker/
  database/
docs/
.github/workflows/
```

The structure will grow as working modules are added; directories are not considered implemented merely because they appear in this plan.

## Development

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for the initial architecture and [docs/LICENSING.md](docs/LICENSING.md) for the content-rights policy.

## Security

Never commit credentials, OAuth client secrets, signing keys, private certificates, or production tokens. Use GitHub Actions secrets or an appropriate secret manager. Google identity tokens must be verified by the backend; never trust a client-supplied user ID.

## Licence

A repository-wide open-source licence will be selected after the initial code and dependency licensing strategy is reviewed. Third-party components retain their own licences.
