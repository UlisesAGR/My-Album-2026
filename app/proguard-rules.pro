# proguard-rules.pro - Module app
# Copyright (c) 2026. All rights reserved

# --- Configuración General y Kotlin ---
# Optimización de atributos esenciales para reflexión y depuración
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable
-dontwarn org.jetbrains.annotations.**

# Preservar metadatos de Kotlin (Evita que las clases selladas y data classes se rompan)
-keep class kotlin.Metadata { *; }
-keep class kotlin.jvm.internal.** { *; }

# --- AndroidX y Navigation ---
-dontwarn androidx.**
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

# --- Google GSON (Crítico para que no truene tu JSON) ---
# Mantiene las anotaciones de SerializedName para que el mapeo no falle
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
# Evita que se ofusquen tus modelos de datos (Ajusta el paquete si es necesario)
-keep class com.speedymovil.wire.core.model.** { *; }
-keep class com.speedymovil.wire.models.** { *; }

# --- Retrofit 2 ---
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keep class * implements retrofit2.Converter { *; }
-keep class * implements retrofit2.CallAdapter { *; }

# --- OkHttp 3 ---
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# --- Coroutines (Versión Limpia sin _releasableHandler) ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }
-dontwarn kotlinx.coroutines.**

# --- Dagger / Hilt (Inyección de dependencias) ---
-keep class dagger.hilt.** { *; }
-keep class * extends androidx.lifecycle.ViewModel
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keepclassmembers class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}
