# Add project specific ProGuard rules here.
# You can update this file to include your application dependencies.

# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Gson
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod

# Data Models (Keep all data classes used in API)
-keep class com.ai.franchise.data.model.** { *; }
-keep class com.ai.franchise.ui.inventory.InventoryItem { *; }
-keep class com.ai.franchise.ui.ai.AIHealthData { *; }
-keep class com.ai.franchise.ui.ai.RiskItem { *; }
-keep class com.ai.franchise.ui.dashboard.OwnerStats { *; }

# ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }

# R8 Full Mode compatibility
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Android components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
