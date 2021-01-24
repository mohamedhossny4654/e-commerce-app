package com.devahmed.techx.onlineshop.Utils;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static Map<String , String> language;


    public static Map<String, String> getLanguage() {
        return language;
    }

    public static void setLanguage(Map<String, String> language) {
        language = language;
    }

    public LanguageManager(String lang) {
        language = new HashMap<>();
        if(lang.equals("en")){
            language.put("enterTheCode" , "Enter The Code");
            language.put("welcomeTo"  , "Welcome to fox mart");
            language.put("signUpToContinue"  , "Sign up to continue ");
            language.put("byPressingSubmit"  , "By pressing you agree to our");
            language.put("weNeedToKnowYourLocation"  , "");
            language.put("GetLocation"  , "Get Location");
            language.put("categories"  , "Categories");
            language.put("search"  , "Search");
            language.put("store"  , "store");
            language.put("orders"  , "Orders");
            language.put("cart"  , "Cart");
            language.put("account"  , "Account");
            language.put("settings"  , "Settings");
            language.put("contactUs"  , "Contact Us");
            language.put("AdminDashboard"  , "Admin Panel");
            language.put("totalPrice"  , "Total Price");
            language.put("status"  , "Status");
            language.put("Users"  , "Users");
            language.put("branches"  , "Branches");
            language.put("bills"  , "Bills");
            language.put("notifications", "Notifications");
            language.put("minCharge"  , "Min Charge");
            language.put("update"  , "Update");
            language.put("send"  , "Send");
            language.put("minValThatUserHasToGet"  , "");
            language.put("call"  , "Call");
            language.put("yourInfo" , "Your Info");
            language.put("phoneNumber"  , "Phone Number");
            language.put("yourName"  , "Your Name");
            language.put("yourAddress"  , "Your Address");
            language.put("area"  , "Area");
            language.put("streetNo"  , "Street No.");
            language.put("buildingNo"  , "Building No.");
            language.put("nearestLandmark"  , "Nearest Landmark");
            language.put("products"  , "Products");
            language.put("yourCartIsEmpty"  , "Your Cart is Empty");
            language.put("goToShopping"  , "Go To Shopping");
            language.put("checkOut"  , "Checkout");
            language.put("total"  , "Total");
            language.put("confirm"  , "Confirm");
            language.put("thankYou"  , "Thank You");
            language.put("yourOrderIs"  , "Your Order Is");
            language.put("inProgress"  , "In Progress");
            language.put("ItemsCount"  , "Items Count");
            language.put("ItemsPrice"  , "Items Price");
            language.put("DeliveryCost"  , "Delivery Cost");
            language.put("enterYourPhone" , "Enter Your Phone");
        }else if(lang.equals("ar")){
            if(lang.equals("ar")){
                language.put("enterTheCode" , "قم بإدخال الرمز");
                language.put("welcomeTo"  , "مرحبا بك");
                language.put("signUpToContinue"  , "سجل دخولك");
                language.put("byPressingSubmit"  , "بالضغط على هذا الزر");
                language.put("weNeedToKnowYourLocation"  , "نحتاج الى معرفة موقعك لتسجيل الدخول");
                language.put("GetLocation"  , "الحصول على الموقع");
                language.put("categories"  , "التصنيفات");
                language.put("enterYourPhone" , "ادخل رقم هاتفك");
                language.put("search"  , "بحث");
                language.put("store"  , "متجر");
                language.put("orders"  , "المشتريات");
                language.put("cart"  , "سلة المشتريات");
                language.put("account"  , "حسابي");
                language.put("settings"  , "الاعدادات");
                language.put("contactUs"  , "تواصل معنا");
                language.put("AdminDashboard"  , "أدارة التطبيق");
                language.put("totalPrice"  , "السعر الاجمالي");
                language.put("status"  , "الحالة");
                language.put("Users"  , "المستخدمون");
                language.put("branches"  , "فروع المحل");
                language.put("bills"  , "الفواتير");
                language.put("notifications", "الاشعارات");
                language.put("minCharge"  , "الحد الادنى للشراء");
                language.put("update"  , "تحديث");
                language.put("send"  , "ارسال");
                language.put("minValThatUserHasToGet"  , "");
                language.put("call"  , "اتصال");
                language.put("yourInfo" , "معلوماتك");
                language.put("phoneNumber"  , "الهاتف");
                language.put("yourName"  , "الاسم");
                language.put("yourAddress"  , "العنوان");
                language.put("area"  , "المنطقة");
                language.put("streetNo"  , "رقم الشارع");
                language.put("buildingNo"  , "رقم العمارة");
                language.put("nearestLandmark"  , "علامة مميزة");
                language.put("products"  , "المنتجات");
                language.put("yourCartIsEmpty"  , "سلة مشترياتك فارغة");
                language.put("goToShopping"  , "قم بالتسوق");
                language.put("checkOut"  , "شراء");
                language.put("total"  , "الاجمالي");
                language.put("confirm"  , "استكمال");
                language.put("thankYou"  , "شكرا لك");
                language.put("yourOrderIs"  , "طلبك الان");
                language.put("inProgress"  , "يتم تجهيزه");
                language.put("ItemsCount"  , "عدد المنتجات");
                language.put("ItemsPrice"  , "سعر المنتجات");
                language.put("DeliveryCost"  , "خدمة التوصيل");
            }
        }


    }
}
