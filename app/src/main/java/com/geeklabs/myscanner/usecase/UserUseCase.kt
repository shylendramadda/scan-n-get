package com.geeklabs.myscanner.usecase

import com.geeklabs.myscanner.local.DataRepository
import com.geeklabs.myscanner.models.User
import io.reactivex.Single
import javax.inject.Inject

class UserUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<User, Single<Unit>>() {

    override fun execute(parameters: User) = Single.just(dataRepository.saveUser(parameters))

    fun getAll() = dataRepository.getAll()
}