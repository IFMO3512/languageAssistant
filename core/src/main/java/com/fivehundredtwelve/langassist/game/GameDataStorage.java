package com.fivehundredtwelve.langassist.game;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by igor on 08.11.14.
 */

/**
 * Stores game data.
 */
public class GameDataStorage implements DataStorage<Object, Object> {

    private ConcurrentHashMap<Object, Object> storage;

    public GameDataStorage() {
        storage = new ConcurrentHashMap<Object, Object>();
    }

    @Override
    public void put(Object key, Object value) {
        storage.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return storage.get(key);
    }

}
