package com.blogspot.e_kanivets.moneytracker.di.module;

import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.controller.TransferController;
import com.blogspot.e_kanivets.moneytracker.entity.Account;
import com.blogspot.e_kanivets.moneytracker.entity.Category;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.entity.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide Controllers dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
public class ControllerModule {

    @Provides
    @NonNull
    @Singleton
    public AccountController providesAccountController(IRepo<Account> accountRepo) {
        return new AccountController(accountRepo);
    }

    @Provides
    @NonNull
    @Singleton
    public CategoryController providesCategoryController(IRepo<Category> categoryRepo) {
        return new CategoryController(categoryRepo);
    }

    @Provides
    @NonNull
    @Singleton
    public ExchangeRateController providesExchangeRateController(IRepo<ExchangeRate> exchangeRateRepo) {
        return new ExchangeRateController(exchangeRateRepo);
    }

    @Provides
    @NonNull
    @Singleton
    public RecordController providesRecordController(IRepo<Record> recordRepo,
                                                     CategoryController categoryController,
                                                     AccountController accountController) {
        return new RecordController(recordRepo, categoryController, accountController);
    }

    @Provides
    @NonNull
    @Singleton
    public TransferController providesTransferController(IRepo<Transfer> transferRepo,
                                                         AccountController accountController) {
        return new TransferController(transferRepo, accountController);
    }
}
