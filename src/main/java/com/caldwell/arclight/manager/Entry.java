package com.caldwell.arclight.manager;

// ********************************************************************************** //
// Title: Arclight                                                                    //
// Author: Gabriel Caldwell                                                           //
// Course Section: CMIS201-ONL1 (Seidel) Fall 2022                                    //
// File: Entry.java                                                                   //
// Description: Data type used for hash table class                                   //
// ********************************************************************************** //

public class Entry<K, V>{

    // fields
    //==================================================================================================================
    int hash;
    K key;
    V val;
    //==================================================================================================================



    // constructor
    //******************************************************************************************************************
    public Entry(K key, V val) {
        this.key = key;
        this.val = val;
        this.hash = key.hashCode();
    }
    //******************************************************************************************************************



    // methods
    //==================================================================================================================
    public boolean equals(Entry<K, V> obj) {
        if (hash != obj.hash) {
            return false;
        }
        return key.equals(obj.key);
    }

    @Override
    public String toString() {
        return "{key: " + key + ", value: " + val + '}';
    }
    //==================================================================================================================

}
