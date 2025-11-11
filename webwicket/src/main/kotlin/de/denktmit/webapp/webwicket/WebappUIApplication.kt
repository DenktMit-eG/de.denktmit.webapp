package de.denktmit.webapp.webwicket

import de.denktmit.webapp.webwicket.error.GenericErrorPage
import de.denktmit.webapp.webwicket.error.Http403ErrorPage
import de.denktmit.webapp.webwicket.error.HttpErrorPage
import de.denktmit.webapp.webwicket.everylayout.EveryLayoutPage
import de.denktmit.webapp.webwicket.fixedcontent.LegalPage
import de.denktmit.webapp.webwicket.admin.AdminUsersPage
import de.denktmit.webapp.webwicket.user.*
import org.apache.wicket.IRequestCycleProvider
import org.apache.wicket.Page
import org.apache.wicket.bean.validation.BeanValidationConfiguration
import org.apache.wicket.core.request.handler.RenderPageRequestHandler
import org.apache.wicket.protocol.http.ResourceIsolationRequestCycleListener
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.resource.loader.BundleStringResourceLoader
import org.apache.wicket.settings.ExceptionSettings
import org.slf4j.LoggerFactory

class WebappUIApplication : WebApplication() {
    override fun getHomePage(): Class<out Page> = Dashboard::class.java

    companion object {
        val LOGGER = LoggerFactory.getLogger(WebappUIApplication::class.java)
    }

    override fun init() {
        super.init()

        cspSettings.blocking().disabled()
        resourceSettings.stringResourceLoaders.add(BundleStringResourceLoader("messages"))

        mountPage("/admin/users", AdminUsersPage::class.java)
        mountPage("/dashboard", Dashboard::class.java)
        mountPage("/every-layout", EveryLayoutPage::class.java)
        mountPage("/impressum", LegalPage::class.java)
        mountPage("/invite-accept", AcceptInvitationPage::class.java)
        mountPage("/legal-notice", LegalPage::class.java)
        mountPage("/me", MePage::class.java)
        mountPage("/recoverPassword", ResetPasswordPage::class.java)
        mountPage("/registration", RegistrationPage::class.java)
        mountPage("/registration-success", RegistrationSuccessPage::class.java)
        mountPage("/user/login", LoginPage::class.java)
        mountPage("/user/logout", LogoutPage::class.java)

        mountPage("/error", GenericErrorPage::class.java)
        mountPage("/error/403", Http403ErrorPage::class.java)
        mountPage("/error/404", HttpErrorPage.Http404ErrorPage::class.java)

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
