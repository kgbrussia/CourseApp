package com.kgbrussia.library.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgbrussia.java.contactdetails.ContactDetailsInteractor
import com.kgbrussia.java.ContactEntity
import com.kgbrussia.java.notification.NotificationInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    private val interactor: ContactDetailsInteractor,
    private val notificationInteractor: NotificationInteractor
) : ViewModel() {

    val contact: LiveData<ContactEntity> get() = mutableContact
    val isLoadingIndicatorVisible: LiveData<Boolean> get() = mutableIsLoadingIndicatorVisible
    val isNotificationEnabled: LiveData<Boolean> get() = mutableIsNotificationEnabled
    private val mutableIsNotificationEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private val mutableContact: MutableLiveData<ContactEntity> = MutableLiveData()
    private val mutableIsLoadingIndicatorVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun contactByIdLoaded(id: String) {
        interactor.loadContactById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mutableIsLoadingIndicatorVisible.postValue(true) }
            .doOnTerminate { mutableIsLoadingIndicatorVisible.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    mutableContact.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    fun checkNotificationState(id: Int) =
        mutableIsNotificationEnabled.postValue(!notificationInteractor.checkNotificationState(id))

    fun newNotification(contact: ContactEntity) {
        val day = contact.dayOfBirthday ?: 1
        val month = contact.monthOfBirthday ?: 1
        notificationInteractor.newNotification(
            contact.id,
            contact.name,
            day,
            month - 1
        )
        checkNotificationState(contact.id)
    }
}