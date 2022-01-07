# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontoptimize
-dontusemixedcaseclassnames
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontpreverify
-verbose
-dontnote com.android.vending.licensing.ILicensingService,com.google.vending.licensing.ILicensingService,com.google.android.vending.licensing.ILicensingService,android.support.**
-dontwarn android.support.**
-ignorewarnings

-dontshrink#混淆jar的时候一定要配置，不然会把没有用到的代码全部remove   我本来封装一个jar就是给别人调用的，全部删掉就没有东西了

-verbose



-keep class com.newolf.widgets.DragLayout{
    public <fields> ;
    public <methods>;
}