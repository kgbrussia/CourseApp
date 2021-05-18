package com.kgbrussia.courseapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgbrussia.courseapp.Contact
import com.kgbrussia.courseapp.ContactRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ContactListViewModel
@Inject constructor(private val ContactProviderRepository: ContactRepository)
    : ViewModel() {

    val contacts: LiveData<List<Contact>> get() = _contacts
    val isLoadingIndicatorVisible: LiveData<Boolean> get() = _isLoadingIndicatorVisible
    private val _contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val _isLoadingIndicatorVisible: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun contactListLoaded(context: Context) {
        ContactProviderRepository.loadContactList("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoadingIndicatorVisible.postValue(true) }
            .doOnTerminate { _isLoadingIndicatorVisible.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    _contacts.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    fun searchQueryEntered(context: Context, subject: PublishSubject<String>) {
        subject.debounce(200, TimeUnit.MILLISECONDS)
            .mergeWith(Observable.just(""))
            .distinctUntilChanged()
            .switchMapSingle { name ->
                ContactProviderRepository.loadContactList(name)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoadingIndicatorVisible.postValue(true) }
            .doOnTerminate { _isLoadingIndicatorVisible.postValue(false) }
            .subscribeBy(
                onNext = {
                    _contacts.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }
}