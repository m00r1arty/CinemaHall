package tj.ikrom.cinemahall.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import tj.ikrom.cinemahall.data.network.api.Api
import tj.ikrom.cinemahall.data.network.repositories.SeatsRepImpl
import tj.ikrom.cinemahall.domain.repositories.SeatsRep

@Module
@InstallIn(ViewModelComponent::class)
object RepModule {

    @Provides
    @ViewModelScoped
    fun provideHistoryRepository(
        api: Api,
    ) : SeatsRep = SeatsRepImpl(api)

}