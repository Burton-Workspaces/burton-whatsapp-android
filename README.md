# Burton Chat

Android messenger with direct chats and groups. The project follows the standard layered Android app structure used by Google’s architecture samples: **UI → domain use cases → repository contracts → data**.

This is a **WhatsApp-style client**, not an unofficial WhatsApp protocol client. Messaging runs locally through Room so you can exercise chats and groups without connecting to WhatsApp’s servers.

License: [Apache-2.0](LICENSE).

## Features

- Chat list with last message, timestamps, and unread counts
- Direct messaging from Contacts
- Group conversations, create/join/leave, and member lists
- Discover groups you are not in yet and join them
- Simulated replies after you send a message

## Architecture

```
app/                    Navigation, Hilt application, activity
core/common             Shared constants and time formatting
core/model              Immutable domain models
core/domain             Repository contracts and use cases
core/data               Room, seed data, repository implementations
core/designsystem       Theme and shared Compose components
feature/chats           Chat list
feature/conversation    Message thread
feature/groups          Groups, create, details
feature/contacts        People and new direct chats
```

Dependencies point inward: feature modules depend on domain, data implements domain, and the app module wires navigation plus Hilt.

## Stack

- Kotlin, Jetpack Compose, Material 3
- Hilt
- Room + Coroutines / Flow
- Unidirectional data flow with ViewModel `StateFlow`

## Run

Open the project in Android Studio or:

```bash
./gradlew :app:assembleGithubDebug
./gradlew :core:domain:test
```

Install the debug APK on a device or emulator running Android 8.0 (API 26) or higher. Seeded conversations and groups appear on first launch.

## Distribution

Two product flavors share the same source:

| Flavor | Task | Channel |
|--------|------|---------|
| `github` | `assembleGithubRelease` | GitHub Releases |
| `fdroid` | `assembleFdroidRelease` | f-droid.org rebuilds |

Burton apps also publish into the shared Droidify catalog:

```bash
./scripts/setup-fdroid-and-secrets.sh
source fdroid-pages.env
./scripts/upload-release-apk.sh 0.1.0
./scripts/publish-fdroid-pages.sh 0.1.0
```

Signing secrets are `KEYSTORE_BASE64` and `KEYSTORE_PASSWORD`. See [DISTRIBUTION.md](DISTRIBUTION.md) and [FDROID.md](FDROID.md).
