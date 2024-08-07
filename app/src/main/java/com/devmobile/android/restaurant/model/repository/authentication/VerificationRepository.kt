package com.devmobile.android.restaurant.model.repository.authentication

import android.content.Context
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmobile.android.restaurant.AccountException
import com.devmobile.android.restaurant.model.entities.User
import com.devmobile.android.restaurant.model.repository.localdata.RestaurantLocalDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException

class VerificationRepository(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val database = RestaurantLocalDatabase.getInstance(context)

    private val _isCodesValid = MutableLiveData<Boolean>()
    val isCodeValid: LiveData<Boolean> = _isCodesValid

    private val _canResendCode = MutableLiveData<Boolean>()
    val canResendCode: LiveData<Boolean> = _canResendCode

    suspend fun codeVerification(codes: Array<String>) {

        return withContext(Dispatchers.IO) {

        }
    }

    suspend fun createAccount(user: User) {
        val userDao = RestaurantLocalDatabase.getInstance(context).getUserDao()

        return withContext(coroutineDispatcher) {

            try {

                userDao.insertUser(user)

            } catch (e: IOException) {

                Log.e(
                    "VerificationRepository",
                    "IO database exception to create account: ${e.message}"
                )
                throw e

            } catch (e: SQLiteDatabaseCorruptException) {

                Log.e(
                    "VerificationRepository", "Database corrupt: ${e.message}"
                )
                throw e

            } catch (e: SQLiteException) {

                Log.e(
                    "VerificationRepository", "SQL exception while creating account: ${e.message}"
                )
                throw e

            } catch (e: AccountException) {

                Log.e(
                    "VerificationRepository",
                    "Unexpected exception while creating account: ${e.message}"
                )
                throw e
            }
        }
    }

    suspend fun verifyCodeEmail() {
        delay(5000)
    }
}