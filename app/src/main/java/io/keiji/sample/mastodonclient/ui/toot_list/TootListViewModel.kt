package io.keiji.sample.mastodonclient.ui.toot_list

import android.app.Application
import androidx.lifecycle.*
import io.keiji.sample.mastodonclient.entity.Account
import io.keiji.sample.mastodonclient.entity.Toot
import io.keiji.sample.mastodonclient.entity.UserCredential
import io.keiji.sample.mastodonclient.repository.AccountRepository
import io.keiji.sample.mastodonclient.repository.TootRepository
import io.keiji.sample.mastodonclient.repository.UserCredentialRerository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TootListViewModel (
    private val instanceUrl: String,
    private val username: String,
    private val timelineType: TimelineType,
    private val coroutineScope: CoroutineScope,
    application: Application
):AndroidViewModel(application),LifecycleObserver{

    private val userCredentialRepository = UserCredentialRerository(application)

    private lateinit var tootRepository : TootRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var userCredential: UserCredential

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        coroutineScope.launch {
            userCredential = userCredentialRepository
                .find(instanceUrl,username) ?: return@launch
            tootRepository = TootRepository(userCredential)
            accountRepository = AccountRepository(userCredential)
            loadNext()
        }
    }

    val isLoading = MutableLiveData<Boolean>()
    var hasNext = true
    val accountInfo = MutableLiveData<Account>()
    val tootList = MutableLiveData<ArrayList<Toot>>()

    fun clear(){
        val tootListSnapshot = tootList.value ?: return
        tootListSnapshot.clear()
    }

    fun loadNext(){
        coroutineScope.launch {
            updateAccountInfo()
            isLoading.postValue(true)

            val tootListSnapshot = tootList.value ?: ArrayList()
            val maxId = tootListSnapshot.lastOrNull()?.id
            val tootListResponse = when (timelineType){
                TimelineType.PublicTimeline -> {
                    tootRepository.fetchPublicTimeline(maxId = maxId,onlyMedia = true)
                }
                TimelineType.HomeTimeline -> {
                    tootRepository.fetchHomeTimeline(maxId = maxId)
                }
            }

            tootListResponse.isNotEmpty()
            tootListSnapshot.addAll(tootListResponse)
            tootList.postValue(tootListSnapshot)

            hasNext = tootListResponse.isNotEmpty()
            isLoading.postValue(false)
        }
    }

    private suspend fun updateAccountInfo() {
        val accountInfoSnapshot = accountInfo.value
            ?: accountRepository.verifyAccountCredential()

        accountInfo.postValue(accountInfoSnapshot)
    }
}

