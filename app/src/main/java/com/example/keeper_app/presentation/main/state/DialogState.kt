package com.example.keeper_app.presentation.main.state

import com.example.keeper_app.data.storage.entities.ServiceDb

sealed interface UiDialog{
    val title: String
    val message: String

    data class Rename(
        val service: ServiceDb,
        override val message: String = ""
    ) : UiDialog {
        override val title: String = "Переименовать сервис"
    }

    data class Delete(
        val service: ServiceDb,
    ) : UiDialog {
        override val title: String = "Удалить сервис"
        override val message: String = "Вы уверены, что хотите удалить сервис «${service.name}»?"
    }

    data class Detail(
        val service: ServiceDb,
    ) : UiDialog {
        override val title: String = service.name
        override val message: String = ""
    }

    object Idle: UiDialog{
        override val title: String = ""
        override val message: String = ""
    }
}