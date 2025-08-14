package tj.ikrom.cinemahall.data.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import tj.ikrom.cinemahall.data.database.AppDatabase

@Module
@InstallIn(ViewModelComponent::class)
object DbModule {

    @Provides
    @ViewModelScoped
    fun providePaymentConditionDao(appDatabase: AppDatabase) =
        appDatabase.paymentDao()


    @Provides
    @ViewModelScoped
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getInstance(appContext)



}