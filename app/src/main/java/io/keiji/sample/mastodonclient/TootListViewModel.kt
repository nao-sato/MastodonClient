package io.keiji.sample.mastodonclient

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TootListViewModel (
    instanceUrl: String,
    private val coroutineScope: CoroutineScope,
    application: Application
):AndroidViewModel(application),LifecycleObserver{

    private val tootRepository = TootRepository(instanceUrl)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loadNext()
    }

    val isLoading = MutableLiveData<Boolean>()
    var hasNext = true

    val tootList = MutableLiveData<ArrayList<Toot>>()

    fun clear(){
        val tootListSnapshot = tootList.value ?: return
        tootListSnapshot.clear()
    }

    fun loadNext(){
        coroutineScope.launch {
            isLoading.postValue(true)

            val tootListSnapshot = tootList.value ?: ArrayList()
            val maxId = tootListSnapshot.lastOrNull()?.id
            val tootListResponse = tootRepository.fetchPublicTimeline(
                maxId = maxId,
                onlyMedia = true
            )
            tootListResponse.isNotEmpty()
            tootList.postValue(tootListSnapshot)

            hasNext = tootListResponse.isNotEmpty()
            isLoading.postValue(false)
        }
    }
}

