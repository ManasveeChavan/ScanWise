# Builds the ScanWise APK in a container with the Android SDK + Gradle preinstalled.
# Usage:
#   docker compose run --rm build
# Output APK will appear in ./app/build/outputs/apk/debug/app-debug.apk on the host
# (mounted via the volume in docker-compose.yml).

FROM eclipse-temurin:17-jdk-jammy

ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV ANDROID_HOME=$ANDROID_SDK_ROOT
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

RUN apt-get update && apt-get install -y --no-install-recommends \
    wget unzip git \
    && rm -rf /var/lib/apt/lists/*

# Install Android command-line tools
RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# Accept licenses and install required SDK components
RUN yes | sdkmanager --licenses > /dev/null && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

WORKDIR /workspace
COPY . .

RUN chmod +x ./gradlew || true

CMD ["./gradlew", "assembleDebug", "--no-daemon"]
