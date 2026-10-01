# Publishing Burton

Package: `com.burton.chat`  
Display name: **Burton**

## 1. One-time setup

```bash
./scripts/setup-fdroid-and-secrets.sh
```

That reuses `~/fdroid` and `../burton-sonos-fdroid`, copies a sibling Burton JKS when
present, then writes GitHub Actions secrets:

| Secret | Value |
|--------|-------|
| `KEYSTORE_BASE64` | Base64 of `release.jks` |
| `KEYSTORE_PASSWORD` | Store password |
| `KEY_ALIAS` | Only if the alias is not `burton` |
| `KEY_PASSWORD` | Only if it differs from the store password |

`*.jks`, `keystore.properties`, and `fdroid-pages.env` are gitignored.

## 2. Local/dev keystore (optional)

Use a **separate** keystore for laptop sideloads. Do **not** put the CI release key in
`keystore.properties` unless you intend to ship with it.

```bash
cp keystore.properties.example keystore.properties
# Point storeFile at a local JKS, or let setup-fdroid-and-secrets.sh create release.jks
```

## 3. Publish a version

Version lives in `version.txt` (currently `0.1.0`). After that file matches the tag:

```bash
source fdroid-pages.env   # written by setup-fdroid-and-secrets.sh
./scripts/upload-release-apk.sh 0.1.0
./scripts/publish-fdroid-pages.sh 0.1.0
```

`upload-release-apk.sh` builds `assembleRelease`, names the artifact
`burton-whatsapp-<version>.apk`, and attaches it to GitHub Release `v<version>`.
`publish-fdroid-pages.sh` indexes that APK into the shared Burton F-Droid catalog.

## 4. Gradle tasks

```bash
./gradlew :app:assembleGithubDebug
./gradlew :app:assembleGithubRelease
./gradlew :app:assembleFdroidRelease
```

Outputs:
- GitHub APK: `app/build/outputs/apk/github/release/Burton-<versionName>.apk`
- F-Droid flavor: `app/build/outputs/apk/fdroid/release/Burton-<versionName>.apk`

See [FDROID.md](FDROID.md) for the Pages catalog.
