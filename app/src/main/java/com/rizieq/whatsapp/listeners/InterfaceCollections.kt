package com.rizieq.whatsapp.listeners

import com.rizieq.whatsapp.utils.StatusListElement

interface ProgressListener {
    fun onProgressUpdate(progress: Int)
}

interface ContactsClickListener {

    fun onContactClicked(name: String?, phone: String?)
}


interface ChatClickListener {
    fun onChatClicked(name: String?, otherUserId: String?, chatsImageUrl: String?,
                      chatsName: String?)
}

interface FailureCallback {
    fun userError()
}

interface StatusItemClickListener {
    fun onItemClicked(statusElement: StatusListElement)
}

