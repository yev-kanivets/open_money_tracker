package com.blogspot.e_kanivets.moneytracker.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.entity.Account;
import com.blogspot.e_kanivets.moneytracker.entity.Category;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.entity.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.repo.TransferRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide {@link IRepo} dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
public class RepoModule {
    private Context context;

    public RepoModule(Context context) {
        this.context = context;
    }

    @Provides
    @NonNull
    @Singleton
    public DbHelper providesDbHelper() {
        return new DbHelper(context);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Account> providesAccountRepo(DbHelper dbHelper) {
        return new AccountRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Category> providesCategoryRepo(DbHelper dbHelper) {
        return new CategoryRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<ExchangeRate> providesExchangeRateRepo(DbHelper dbHelper) {
        return new ExchangeRateRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Record> providesRecordRepo(DbHelper dbHelper) {
        return new RecordRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Transfer> providesTransferRepo(DbHelper dbHelper) {
        return new TransferRepo(dbHelper);
    }
}
