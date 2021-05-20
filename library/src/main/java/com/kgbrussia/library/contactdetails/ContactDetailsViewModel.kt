package com.kgbrussia.library.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgbrussia.java.contactdetails.ContactDetailsInteractor
import com.kgbrussia.java.ContactEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactDetailsViewModel
@Inject constructor(private val interactor: ContactDetailsInteractor)
    : ViewModel() {

    val contact: LiveData<ContactEntity> get() = _contact
    val isLoadingIndicatorVisible: LiveData<Boolean> get() = _isLoadingIndicatorVisible
    private val _contact: MutableLiveData<ContactEntity> = MutableLiveData()
    private val _isLoadingIndicatorVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun contactByIdLoaded(id: String) {
        interactor.loadContactById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoadingIndicatorVisible.postValue(true) }
            .doOnTerminate { _isLoadingIndicatorVisible.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    _contact.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }
}