package com.kgbrussia.courseapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ContactListViewModel : ViewModel() {

    val contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun getContactList(context: Context) {
        ContactLoaderRepository.loadContactList(context, "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    contacts.postValue(it)
                },
                onError = {
                }
            )
            .addTo(disposable)
    }

    fun getFilteredContactList(context: Context, subject: PublishSubject<String>) {
        subject.debounce(10, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle { name ->
                ContactLoaderRepository.loadContactList(context, name)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    contacts.postValue(it)
                },
                onError = {
                }
            )
            .addTo(disposable)
    }
}

class ContactDetailsViewModel : ViewModel() {

    val contact: MutableLiveData<Contact> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun getContactById(context: Context, id: String) {
        ContactLoaderRepository.loadContact(context, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    contact.postValue(it)
                },
                onError = {
                }
            )
            .addTo(disposable)
    }
}

object ContactLoaderRepository : ContactRepository {

    override fun loadContactList(context: Context, name: String): Single<List<Contact>> =
        Single.fromCallable<List<Contact>> { ContactResolver.getContactsList(context, name) }

    override fun loadContact(context: Context, id: String): Single<Contact> =
        Single.fromCallable<Contact> { ContactResolver.findContactById(context, id) }

}

interface ContactRepository {
    fun loadContactList(context: Context, name: String): Single<List<Contact>>
    fun loadContact(context: Context, id: String): Single<Contact>
}