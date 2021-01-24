package com.devahmed.techx.onlineshop.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Shared Carterence
 */
public class CartManager {

    private Context context;
    private String catrProductsList[];
    private String _CartName;

    public CartManager(Context context) {
        this.context = context;
        catrProductsList = getKeysAsSingleString().split(",");
        _CartName = "default";
    }

    public CartManager(Context context , String _CartName) {
        this.context = context;
        catrProductsList = getKeysAsSingleString().split(",");
        this._CartName = _CartName;
    }

    public void addToCart(String postKey) {
        if(postKey.trim().isEmpty() || postKey.equals("") || postKey == null){
            System.out.println("Try to save a null or empty key");
            return;
        }
        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
        //get posts that already stored
        String posts = getKeysAsSingleString();
        String[] postsKeysList = posts.split(",");
        //create new array with additional place for new post key
        String[] postsKeysList2 = new String[postsKeysList.length + 1];
        for (int i = 0; i <postsKeysList.length ; i++) {
            postsKeysList2[i] = postsKeysList[i];
        }
        //add the post key at the end of new array
        postsKeysList2[postsKeysList.length] = postKey;
        //add all keys in one stringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < postsKeysList2.length; i++) {
            sb.append(postsKeysList2[i]);
            sb.append(",");
        }
        //add the stringBuilder to sharedCarterences
        SharedPreferences.Editor editor = sharedCarterences.edit();
        editor.putString(_CartName,sb.toString());
        editor.commit();
        catrProductsList = postsKeysList2;
    }

    private String getKeysAsSingleString() {
        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
        //get posts that already stored
        String posts = sharedCarterences.getString(_CartName , "");
        return posts;
    }

    public void deleteAllKeys(){
        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedCarterences.edit();
        editor.putString(_CartName,"");
        editor.commit();
    }

    //delete a specific key from keys list
    public void deleteKey(String keyToDelete){
        String [] tempArray = new String[catrProductsList.length];
        boolean ok = false;
        for (int i = 0; i < catrProductsList.length ; i++) {
            if(catrProductsList[i].equals(keyToDelete)){
                //skip this post and don't add it in the new tempArray
            }else{
                tempArray[i] = catrProductsList[i];
                ok = true;
            }
        }

        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tempArray.length; i++) {
            if(tempArray[i] != null ){
                sb.append(tempArray[i]);
                sb.append(",");
            }
        }
        //add the stringBuilder to sharedCarterences
        SharedPreferences.Editor editor = sharedCarterences.edit();
        editor.putString(_CartName,sb.toString());
        editor.commit();

        if(ok){
            catrProductsList = tempArray;
            System.out.println( keyToDelete + " key deleted successfully");
        }else{
            System.out.println("key not found !");
            return;
        }
        catrProductsList = getKeysAsSingleString().split(",");
    }

    public void deleteKey(int indexToDelete){
        indexToDelete++;
        boolean ok = false;
        // Create another array of size one less
        String [] tempArray = new String[catrProductsList.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < tempArray.length; i++) {

            // if the index is
            // the removal element index
            if (i == indexToDelete) {
                ok = true;
                continue;
            }

            // if the index is not
            // the removal element index
            tempArray[k++] = catrProductsList[i];
        }

        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tempArray.length; i++) {
                if(tempArray[i] != null ){
                    sb.append(tempArray[i]);
                    sb.append(",");
                }
            }
            //add the stringBuilder to sharedCarterences
            SharedPreferences.Editor editor = sharedCarterences.edit();
            editor.putString(_CartName,sb.toString());
            editor.commit();

            if(ok){
                catrProductsList = tempArray;
                System.out.println("key deleted successfully");
            }else{
                System.out.println("key not found !");
                return;
            }
            catrProductsList = getKeysAsSingleString().split(",");
    }

    public void replaceKey(int index , String newKey){
        index++;
        if(newKey.trim().isEmpty() || newKey.equals("") || newKey == null){
            System.out.println("Try to save a null or empty key");
            return;
        }
        SharedPreferences sharedCarterences = context.getSharedPreferences(_CartName, Context.MODE_PRIVATE);
        //get posts that already stored
        String posts = getKeysAsSingleString();
        String[] postsKeysList = posts.split(",");
        //create new array with additional place for new post key
        String[] postsKeysList2 = new String[postsKeysList.length];
        for (int i = 0; i <postsKeysList.length ; i++) {
            if(i == index){
                //add the new key to this specific index
                postsKeysList2[i] = newKey;
            }else{
                postsKeysList2[i] = postsKeysList[i];
            }
        }
        //add all keys in one stringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < postsKeysList2.length; i++) {
            sb.append(postsKeysList2[i]);
            sb.append(",");
        }
        //add the stringBuilder to sharedCarterences
        SharedPreferences.Editor editor = sharedCarterences.edit();
        editor.putString(_CartName,sb.toString());
        editor.commit();
        catrProductsList = postsKeysList2;
    }

    //check if this key is exist in keys list
    public boolean has (String postKey){
        for (int i = 0; i < catrProductsList.length ; i++) {
            if(catrProductsList[i].equals(postKey)){
                return true;
            }
        }
        return false;
    }

    public List<String> getStoredValues(){
        String posts = getKeysAsSingleString();
        String[] postsKeysList = posts.split(",");
        List<String> list = new ArrayList<>();
        for(String s : postsKeysList){
            list.add(s);
        }
        //always remove object at index 0 because its = "";
        try {
            list.remove(0);
        }catch (IndexOutOfBoundsException e){
            String message = e.getMessage();
            System.out.println(message);
        }

        return list;
    }

}