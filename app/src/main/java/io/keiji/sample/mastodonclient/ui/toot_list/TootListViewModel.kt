package io.keiji.sample.mastodonclient.ui.toot_list

import android.app.Application
import androidx.lifecycle.*
import io.keiji.sample.mastodonclient.entity.Account
import io.keiji.sample.mastodonclient.entity.Toot
import io.keiji.sample.mastodonclient.entity.UserCredential
import io.keiji.sample.mastodonclient.repository.AccountRepository
import io.keiji.sample.mastodonclient.repository.TootRepository
import io.keiji.sample.mastodonclient.repository.UserCredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class TootListViewModel (
    private val instanceUrl: String,
    private val username: String,
    private val timelineType: TimelineType,
    private val coroutineScope: CoroutineScope,
    application: Application
):AndroidViewModel(application),LifecycleObserver {

    val loginRequired = MutableLiveData<Boolean>()

    private val userCredentialRepository = UserCredentialRepository(application)

    private lateinit var tootRepository: TootRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var userCredential: UserCredential

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        reloadUserCredential()
    }

    val isLoading = MutableLiveData<Boolean>()
    var hasNext = true
    val errorMessage = MutableLiveData<String>()
    val accountInfo = MutableLiveData<Account>()
    val tootList = MutableLiveData<ArrayList<Toot>>()

    fun clear() {
        val tootListSnapshot = tootList.value ?: return
        tootListSnapshot.clear()
    }

    fun loadNext() {
        coroutineScope.launch {
            updateAccountInfo()
            isLoading.postValue(true)

            val tootListSnapshot = tootList.value ?: ArrayList()
            val maxId = tootListSnapshot.lastOrNull()?.id

            try {
                val tootListResponse = when (timelineType) {
                    TimelineType.PublicTimeline -> {
                        tootRepository.fetchPublicTimeline(maxId = maxId, onlyMedia = true)
                    }
                    TimelineType.HomeTimeline -> {
                        tootRepository.fetchHomeTimeline(maxId = maxId)
                    }
                }

                val newTootList = ArrayList(tootListSnapshot)
                    .also {
                        it.addAll(tootListResponse )
                    }
                tootList.postValue(newTootList)
                hasNext = tootListResponse.isNotEmpty()

            }catch (e: HttpException) {
                when (e.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        errorMessage.postValue("必要な権限がありません")
                    }
                }
            }catch (e: IOException){
                errorMessage.postValue("サーバーに接続できませんでした。${e.message}")
            }finally {
                isLoading.postValue(false)
            }
        }
    }

    private suspend fun updateAccountInfo() {

        try {
            val accountInfoSnapshot = accountInfo.value
                ?: accountRepository.verifyAccountCredential()

            accountInfo.postValue(accountInfoSnapshot)
        }catch (e: HttpException){
            when(e.code()){
                HttpURLConnection.HTTP_FORBIDDEN ->{
                    errorMessage.postValue("必要な権限がありません")
                }
            }
        }catch (e: IOException){
            errorMessage.postValue("サーバーに接続できません。${e.message}")
        }
    }

    fun delete(toot: Toot) {
        coroutineScope.launch {
            try {
                tootRepository.delete(toot.id)

                val tootListSnapshot = tootList.value ?: ArrayList()
                val newTootList = ArrayList(tootListSnapshot)
                    .also {
                        it.remove(toot)
                    }
                tootList.postValue(newTootList)
            }catch (e: HttpException) {
                when(e.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        errorMessage.postValue("必要な権限かありません")
                    }
                }
            }catch (e: IOException){
                errorMessage.postValue("サーバーに接続できませんでした。")
            }
        }
    }

    fun reloadUserCredential() {
        coroutineScope.launch {
            val credential = userCredentialRepository
                .find(instanceUrl, username)
            if (credential == null) {
                loginRequired.postValue(true)
                return@launch
            }
            tootRepository = TootRepository(credential)
            accountRepository = AccountRepository(credential)
            userCredential = credential

            clear()
            loadNext()
        }
    }
}

