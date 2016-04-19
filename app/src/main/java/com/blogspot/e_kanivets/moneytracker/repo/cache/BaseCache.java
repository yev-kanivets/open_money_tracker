package com.blogspot.e_kanivets.moneytracker.repo.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.List;

/**
 * Cache for {@link IRepo} instances. Use Decorator pattern.
 * Created on 4/19/16.
 *
 * @author Evgenii Kanivets
 */
public class BaseCache<T> implements IRepo<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseCache";

    @NonNull
    private IRepo<T> repo;

    public BaseCache(@NonNull IRepo<T> repo) {
        this.repo = repo;
    }

    @Nullable
    @Override
    public T create(@Nullable T instance) {
        return repo.create(instance);
    }

    @Nullable
    @Override
    public T read(long id) {
        return repo.read(id);
    }

    @Nullable
    @Override
    public T update(@Nullable T instance) {
        return repo.update(instance);
    }

    @Override
    public boolean delete(@Nullable T instance) {
        return repo.delete(instance);
    }

    @NonNull
    @Override
    public List<T> readAll() {
        return repo.readAll();
    }

    @NonNull
    @Override
    public List<T> readWithCondition(@Nullable String condition, @Nullable String[] args) {
        return repo.readWithCondition(condition, args);
    }
}
