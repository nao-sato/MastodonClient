package io.keiji.sample.mastodonclient.ui.toot_detail

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import io.keiji.sample.mastodonclient.entity.Toot
import kotlinx.coroutines.CoroutineScope

class TootDetailViewModelFactory (
    private val toot: Toot?,
    private val coroutineScope: CoroutineScope,
    private val context: Context,
    ):ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TootDetailViewModel(
            toot,
            coroutineScope,
            context.applicationContext as Application
        ) as T
    }
}