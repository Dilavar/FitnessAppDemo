package com.example.fitnessapp.di

import android.app.Application
import androidx.room.Room
import com.example.fitnessapp.data.datasource.FitnessDatabase
import com.example.fitnessapp.data.repository.StepRepositoryImpl
import com.example.fitnessapp.domain.repository.StepsRepository
import com.example.fitnessapp.domain.use_cases.GetDayStepsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabaseObject(app: Application): FitnessDatabase {
        return Room.databaseBuilder(
            app,
            FitnessDatabase::class.java,
            FitnessDatabase.FITNESS_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideStepRepository(db:FitnessDatabase):StepsRepository{
        return StepRepositoryImpl(db.stepsDao)
    }

    @Provides
    @Singleton
    fun provideStepUseCase(repository: StepsRepository):GetDayStepsUseCase{
        return GetDayStepsUseCase(repository)
    }

}
