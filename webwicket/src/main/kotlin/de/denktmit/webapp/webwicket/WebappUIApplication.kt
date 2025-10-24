package de.denktmit.webapp.webwicket

import de.denktmit.webapp.webwicket.error.GenericErrorPage
import de.denktmit.webapp.webwicket.error.Http403ErrorPage
import de.denktmit.webapp.webwicket.error.HttpErrorPage
import de.denktmit.webapp.webwicket.everylayout.EveryLayoutPage
import de.denktmit.webapp.webwicket.fixedcontent.LegalPage
import de.denktmit.webapp.webwicket.admin.AdminUsersPage
import org.apache.wicket.IRequestCycleProvider
import org.apache.wicket.Page
import org.apache.wicket.bean.validation.BeanValidationConfiguration
import org.apache.wicket.core.request.handler.RenderPageRequestHandler
import org.apache.wicket.protocol.http.ResourceIsolationRequestCycleListener
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.settings.ExceptionSettings

class WebappUIApplication : WebApplication() {
    override fun getHomePage(): Class<out Page> = Dashboard::class.java

    override fun init() {
        super.init()

        cspSettings.blocking().disabled()

        mountPage("/p/dashboard", Dashboard::class.java)
        mountPage("/p/legal-notice", LegalPage::class.java)
        mountPage("/p/impressum", LegalPage::class.java)
        mountPage("/p/every-layout", EveryLayoutPage::class.java)
        mountPage("/p/admin/users", AdminUsersPage::class.java)

        mountPage("/p/error", GenericErrorPage::class.java)
        mountPage("/p/error/403", Http403ErrorPage::class.java)
        mountPage("/p/error/404", HttpErrorPage.Http404ErrorPage::class.java)

        applicationSettings.setInternalErrorPage(GenericErrorPage::class.java)
        exceptionSettings.setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE)
        applicationSettings.setAccessDeniedPage(Http403ErrorPage::class.java)

        BeanValidationConfiguration().configure(this)

        requestCycleListeners.add(ResourceIsolationRequestCycleListener())
        requestCycleProvider =
            IRequestCycleProvider { ctx ->
                object : RequestCycle(ctx) {
                    override fun handleException(e: Exception?): IRequestHandler {
                        val errorPage = GenericErrorPage(e, PageParameters())
                        return RenderPageRequestHandler(errorPage)
                    }
                }
            }
    }
}
