# Content licensing and rights policy

Experience TV must not stream a title merely because it is discoverable online or available in a media-server library.

## Acceptable sources

- Direct studio, distributor, filmmaker, or rights-holder agreements.
- Original productions with documented chain of title.
- Public-domain works after verifying status in each target territory.
- Openly licensed works after checking the exact licence, version, attribution, commercial-use permissions, and any share-alike or other conditions.

A title's availability in one country does not establish rights worldwide.

## Required title-rights record

Record at minimum:

- Rights holder and evidence/contract reference.
- Title and specific version/asset covered.
- Permitted territories.
- Rights start and end timestamps, including timezone.
- Permitted uses: streaming, download, promotional clips, and/or other uses.
- Permitted device classes and maximum resolution, if restricted.
- Language, audio, subtitle, and accessibility obligations.
- Advertising, subscription, and revenue-share restrictions.
- Attribution text and licence identifier where applicable.
- Internal reviewer, approval date, and audit trail.

## Enforcement

The backend is authoritative. Before creating a playback session, check the viewer's territory, current rights window, profile maturity settings, subscription/entitlement, device restrictions, and permitted playback method. Do not rely on hiding a title in the Android UI.

Downloads require separate explicit rights and secure expiry/revocation handling. Premium DRM such as Widevine requires appropriate commercial provisioning and a licence service; adding a player library does not grant DRM rights.

## Third-party software

Jellyfin, FFmpeg, AndroidX Media3, and all other dependencies retain their own licences and notices. Do not copy Jellyfin source into this repository under a different licence without complying with its licence. Review dependency licences before choosing a repository-wide licence.
