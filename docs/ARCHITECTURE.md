# Experience TV architecture

## Principles

1. Mobile and TV are separate Android applications with separate application IDs and release pipelines.
2. Both clients use a versioned backend API; neither talks directly to the database or media-server administration API.
3. Identity, profiles, catalogue, rights, playback sessions, and watch progress are owned by the Experience TV backend.
4. Media delivery is separated from catalogue and account APIs.
5. Provider-specific media-server logic is isolated behind an adapter so Jellyfin can be replaced.
6. Enforce rights and entitlements on the server for every playback session.

## Logical components

- **Mobile client:** Kotlin, Jetpack Compose, Android 9 / API 28 minimum.
- **Android TV client:** Kotlin, Compose for TV, remote/D-pad focus handling, Android TV 9 / API 28 minimum.
- **API:** Kotlin/JVM Spring Boot modular monolith for the first release.
- **Identity:** unified Experience Account; Google sign-in is a linked identity provider. Verify Google ID tokens on the backend. Keep OAuth secrets out of APKs.
- **Catalogue:** titles, seasons, episodes, genres, artwork, audio/subtitle tracks, maturity metadata.
- **Rights and entitlements:** title/territory/time/device/resolution/use restrictions checked before issuing playback access.
- **Playback:** create short-lived playback sessions and return only permitted stream/manifest URLs and DRM configuration.
- **Media adapter:** provider-neutral interface; Jellyfin is the first development integration.
- **Studio:** restricted web dashboard for ingestion, metadata, assets, subtitles, rights windows, publishing, and audit history.

## Initial data domains

Users, linked auth identities, profiles, devices, titles, seasons, episodes, genres, artwork, media assets, audio tracks, subtitle tracks, rights windows, entitlements, plans, subscriptions, playback sessions, watch history, and watchlists.

Use database migrations from the first schema change. Store secrets outside the database where possible. Do not store video binaries in PostgreSQL.

## Playback lifecycle

1. Client requests playback for a profile and title.
2. API authenticates the user and verifies profile access.
3. Rights service checks territory, current rights window, permitted device class, resolution, and entitlement.
4. Playback service creates a short-lived session and asks the media adapter for a stream.
5. Client plays with AndroidX Media3.
6. Client periodically reports progress; API validates session ownership and stores watch history.
7. Continue Watching is derived from server-side progress and is shared between devices.

## TV sign-in

Use a short-lived device code and a separate approval flow on a signed-in phone/browser. The QR code must not contain an access token or refresh token. Expire codes quickly, rate-limit polling, and bind approval to the initiating device session.

## Infrastructure stages

### Development

Docker Compose for local backend and Jellyfin; managed PostgreSQL free tier may be used for prototypes. Treat free-tier databases and storage as disposable development infrastructure and keep migrations/backups.

### Production

Use persistent compute for the API and media services, durable object storage for media, background workers for FFmpeg packaging/transcoding, and a CDN for delivery. Add monitoring, alerting, backups, abuse controls, and budget limits before public launch.

Cloudflare Workers may host lightweight edge/API functions but are not the Jellyfin host, video origin, or FFmpeg worker. Supabase database/storage free tiers are not a production video-delivery plan.

## Quality gates

- Independent debug builds for both Android apps.
- Unit tests for identity linking, rights windows, entitlement checks, and progress merging.
- Integration tests for API and media adapter.
- Device tests for codecs, subtitles, audio selection, seeking, and TV D-pad navigation.
- Never claim 4K, HDR, Widevine level, or offline playback until validated on target devices and licensed infrastructure.
