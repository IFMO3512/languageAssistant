package com.fivehundredtwelve.langassist.game;

/**
 * Created by igor on 10.11.14.
 */

/**
 * Represents data storage interface.
 */
public interface DataStorage<KeyType, ValueType> {

    /**
     * Puts data in storage.
     *
     * @param key   data key
     * @param value data value
     */
    public void put(KeyType key, ValueType value);

    /**
     * Gets data from storage.
     *
     * @param key data key
     * @return data value
     */
    public ValueType get(KeyType key);
}
