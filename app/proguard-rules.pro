# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK tools proguard configuration.

# Keep data classes
-keepclassmembers class com.aureon.app.** { *; }

# Keep Kotlin metadata
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
