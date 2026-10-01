# F-Droid / Droidify repository

GitHub Releases are APK downloads. [Droidify](https://github.com/Droid-ify/client) and the
official F-Droid client do **not** subscribe to those. They need an **F-Droid repository**:
a public HTTPS folder with signed APKs plus a signed catalog (`index-v1.jar`).

This is a **self-hosted simple binary repo** of the same APKs the upload script already
signs. It is not submission to [f-droid.org](https://f-droid.org/), which rebuilds from
source and signs with F-Droid’s key.

Burton Android apps share one Pages catalog:
[Burton-Workspaces/burton-sonos-fdroid](https://github.com/Burton-Workspaces/burton-sonos-fdroid).

## One-time setup

```bash
./scripts/setup-fdroid-and-secrets.sh
```

That installs `fdroidserver` (via pipx, not Debian apt), reuses or creates `~/fdroid`,
clones the Pages repo, copies `fdroid/metadata/com.burton.chat.yml` into the working tree,
and writes this app’s GitHub signing secrets (`KEYSTORE_BASE64` / `KEYSTORE_PASSWORD`).

Do **not** use Debian’s `apt install fdroidserver` (2.2.1). That Androguard cannot scan
AGP 8.7+ APKs.

## Publish

```bash
source fdroid-pages.env
./scripts/upload-release-apk.sh 0.1.0
./scripts/publish-fdroid-pages.sh 0.1.0
```

`FDROID_ASSEMBLE=1` forces `assembleRelease`. `FDROID_PAGES_PUSH=0` commits without pushing.

The version argument must match `version.txt`.

## Two keys

| Key | File | What it signs |
| --- | --- | --- |
| **App signing key** | `release.jks` (alias `burton`) | The APK |
| **Repo signing key** | Created by `fdroid init` | The repo index (`index-v1.jar`) |

Never publish `config.yml` or the repo keystore.

Add the catalog in Droidify:

`https://burton-workspaces.github.io/burton-sonos-fdroid/fdroid/repo`

Flavor `fdroid` (`assembleFdroidRelease`) is still available for an f-droid.org rebuild.
The Pages catalog publishes the same signed GitHub APK as the other Burton apps.
