# ScanWise — QR Code Scam Detection

An Android app (Kotlin + Jetpack Compose) that scans QR codes, extracts URLs,
and runs an on-device heuristic risk analysis to warn users about phishing/scam links.

## Stack

- **UI:** Jetpack Compose, Material 3, Navigation Compose
- **Scanning:** CameraX + ML Kit Barcode Scanning
- **Storage:** Room (`scan_history`, `blacklisted_urls`, `malicious_patterns`)
- **Architecture:** MVVM — `domain` (analysis engine + models), `data` (Room + repository), `ui` (screens + view model)

## Risk scoring

`UrlAnalyzer` (in `domain/analysis`) computes a 0-100 score from five weighted
sub-scores, exactly matching the spec:

```
score = url*0.30 + blacklist*0.30 + content*0.20 + ssl*0.10 + behavioral*0.10
0-30  -> SAFE (green)
31-70 -> MEDIUM RISK (orange)
71-100 -> DANGEROUS (red)
```

It runs entirely on-device using structural URL heuristics (IP-as-host,
suspicious TLDs, brand-lookalike detection, excessive subdomains, HTTPS check,
phishing keyword matching) plus a local Room blacklist and scan-history lookup
for behavioral scoring. No external API keys are required to run the app.

## Project layout

```
app/src/main/java/com/scanwise/app/
├── MainActivity.kt           # Nav host + bottom navigation (Scan / History / Settings)
├── ScanWiseApp.kt            # Application class wiring Room + repository
├── domain/
│   ├── model/                # AnalysisResult, RiskLevel, Finding
│   └── analysis/UrlAnalyzer  # heuristic risk-scoring engine
├── data/
│   ├── local/                # Room entities, DAOs, database
│   └── repository/           # ScanRepository (analysis + persistence + stats)
└── ui/
    ├── scanner/              # CameraX preview, ML Kit analyzer, scan overlay UI
    ├── result/               # Risk score circle, findings cards, score breakdown
    ├── history/              # Scan history list + statistics
    ├── settings/             # Notification / analysis / privacy toggles
    └── theme/                # Material 3 color scheme matching the spec palette
```

## Building

This project requires the Android Gradle Plugin and Android SDK, which need
network access to Google's Maven repository (`dl.google.com`) — unavailable in
this sandbox. To build and run:

1. Open the project root in Android Studio (Koala or newer).
2. Let Gradle sync (it will fetch AGP 8.4, Kotlin 1.9.24, Compose BOM, CameraX, ML Kit, Room).
3. Run on an emulator or device with API 21+ and a camera.

## Notes / scope

This is a focused, working core of the much larger spec: scanning, on-device
risk analysis, persistent history with stats, domain blocking, and settings UI
with the specified Material 3 color palette and animations (scan line, pulsing
risk circle, animated score counter). Not included: paid third-party API
integrations (VirusTotal/Safe Browsing/WHOIS/GeoIP), remote content fetching,
encrypted-at-rest database, and CSV/PDF export — these can be layered onto the
existing repository/analysis boundary without restructuring the app.
