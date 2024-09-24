package com.developerali.masterstroke.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class MarkedItemsPreference {

    private static final String PREF_NAME = "marked_items_pref";
    private static final String MARKED_ITEMS_KEY = "marked_items";

    // Save an item to the marked items set
    public static void saveMarkedItem(Context context, String itemId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Create a new set from the current set to avoid modifying the original reference
        Set<String> markedItems = new HashSet<>(prefs.getStringSet(MARKED_ITEMS_KEY, new HashSet<>()));

        markedItems.add(itemId); // Add the new item
        prefs.edit().putStringSet(MARKED_ITEMS_KEY, markedItems).apply(); // Save the updated set
    }

    // Remove an item from the marked items set
    public static void removeMarkedItem(Context context, String itemId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Create a new set from the current set to avoid modifying the original reference
        Set<String> markedItems = new HashSet<>(prefs.getStringSet(MARKED_ITEMS_KEY, new HashSet<>()));

        if (markedItems.contains(itemId)) {
            markedItems.remove(itemId); // Remove the item
            prefs.edit().putStringSet(MARKED_ITEMS_KEY, markedItems).apply(); // Save the updated set
        }
    }

    // Retrieve all marked items
    public static Set<String> getMarkedItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Return a copy of the set to avoid unintended modifications
        return new HashSet<>(prefs.getStringSet(MARKED_ITEMS_KEY, new HashSet<>()));
    }
}

