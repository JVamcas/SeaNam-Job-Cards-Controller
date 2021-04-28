package com.jvmtechs.app

import com.jvmtechs.controllers.user.LoginController
import com.jvmtechs.utils.SessionManager
import tornadofx.*

class SeaNamJobCardApp: App(MainWorkspace::class, Styles::class){

    override fun onBeforeShow(view: UIComponent) {
        super.onBeforeShow(view)
        workspace.dock<LoginController>()
    }

    override fun stop() {
        super.stop()
        try{
            SessionManager.newInstance?.close()
        }
        catch (e: Exception){

        }
    }
}