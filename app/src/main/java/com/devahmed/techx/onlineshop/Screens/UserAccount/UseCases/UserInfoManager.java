package com.devahmed.techx.onlineshop.Screens.UserAccount.UseCases;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoManager {

    private Context context;
    private SharedPreferences sharedCarterences;
    private SharedPreferences.Editor editor;
    private String phone ;
    private String name ;
    private String area ;
    private String street ;
    private String flat ;
    private String uniqueSigns ;


    public UserInfoManager(Context context) {
        this.context = context;
        sharedCarterences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedCarterences.edit();
    }

    public void saveUserData(String phone , String name , String area
                            , String street , String flat , String uniqueSigns){
        //add the stringBuilder to sharedCarterences
        editor.putString("phone" , phone);
        editor.putString("name" , name);
        editor.putString("area" , area);
        editor.putString("street" , street);
        editor.putString("flat" , street);
        editor.putString("uniqueSigns" , uniqueSigns);
        editor.commit();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        editor.putString("phone" , phone);
        editor.commit();
    }

    public void setName(String name) {
        this.name = name;
        editor.putString("name" , name);
        editor.commit();
    }

    public void setArea(String area) {
        this.area = area;
        editor.putString("area" , area);
        editor.commit();
    }

    public void setStreet(String street) {
        this.street = street;
        editor.putString("street" , street);
        editor.commit();
    }

    public void setFlat(String flat) {
        this.flat = flat;
        editor.putString("flat" , flat);
        editor.commit();
    }

    public void setUniqueSigns(String uniqueSigns) {
        this.uniqueSigns = uniqueSigns;
        editor.putString("uniqueSigns" , uniqueSigns);
        editor.commit();
    }

    public String getPhone() {

        return sharedCarterences.getString("phone" , "");
    }

    public String getName() {
        return sharedCarterences.getString("name" , "");
    }

    public String getArea() {
        return sharedCarterences.getString("area" , "");
    }

    public String getStreet() {
        return sharedCarterences.getString("street" , "");
    }

    public String getFlat() {
        return sharedCarterences.getString("flat" , "");
    }

    public String getUniqueSigns() {
        return sharedCarterences.getString("uniqueSigns" , "");
    }

    public int getPoints() {
        return sharedCarterences.getInt("points" , 0);
    }

}
