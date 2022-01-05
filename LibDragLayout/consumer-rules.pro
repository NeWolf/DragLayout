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
    public <fields>;
    public <methods>;
}