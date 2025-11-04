package de.denktmit.webapp.webwicket.admin

import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.springconfig.WicketContextProperties
import de.denktmit.webapp.webwicket.layout.SidebarBasePage
import de.denktmit.webapp.webwicket.user.AcceptInvitationPage
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.component.DmDropDownChoice
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.form.DmCheckbox
import de.denktmit.wicket.components.form.DmForm
import de.denktmit.wicket.spring.bean
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.model.*
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.validation.IValidator
import org.apache.wicket.validation.validator.EmailAddressValidator
import org.apache.wicket.validation.validator.StringValidator
import java.io.Serializable
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AdminUsersPage(
    pageParameters: PageParameters?,
) : SidebarBasePage(pageParameters, AdminSidebarPanel()) {

    @delegate:Transient
    private val userService: UserService by bean()

    @delegate:Transient
    private val wicketContextProperties: WicketContextProperties by bean()

    @Transient
    private val formatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(session.locale)
        .withZone(ZoneId.systemDefault())

    // Decide whether to show the invite form or the list based on the query parameter
    private val inviteMode: Boolean =
        pageParameters?.get("action")?.toString()?.equals("invite", ignoreCase = true) ?: false

    // Data model for the users table
    data class UserRow(
        val email: String,
        val disabled: Boolean,
        val lockedUntil: String,
        val accountValidUntil: String,
        val credentialsValidUntil: String,
        var selected: Boolean = false,
    )

    // Form backing bean + model (best practice: keep state in a model)
    data class InvitationForm(var emailAddress: String? = null, var groupName: String? = null) : Serializable

    private val usersModel: IModel<List<UserRow>> = object : LoadableDetachableModel<List<UserRow>>() {
        override fun load(): List<UserRow> {
            return userService.userDataV2().map {
                UserRow(
                    it.user.mail,
                    it.user.disabled,
                    formatter.format(it.user.lockedUntil),
                    formatter.format(it.user.accountValidUntil),
                    formatter.format(it.user.credentialsValidUntil),
                )
            }
        }
    }

    // Choices for the group dropdown (replace with service/DB lookup later)
    private val groupChoicesModel: IModel<List<String>> = object : LoadableDetachableModel<List<String>>() {
        override fun load(): List<String> = listOf("Users", "Managers", "Admins")
    }

    // Backing model for the invitation form
    private val invitationFormModel: IModel<InvitationForm> = CompoundPropertyModel(Model(InvitationForm()))

    override fun onInitialize() {
        super.onInitialize()
        sidebarPanel.isVisible = true

        +DmContainer("listSection") {
            isVisible = !inviteMode
            add(DmForm("tableForm", usersModel) {
                +DmListView("rows", model) {
                    +DmCheckbox(it::selected)
                    +DmLabel("email", it.email)
                    +DmLabel("disabled", it.disabled.toString())
                    +DmLabel("lockedUntil", it.lockedUntil)
                    +DmLabel("accountValidUntil", it.accountValidUntil)
                    +DmLabel("credentialsValidUntil", it.credentialsValidUntil)
                }
                onSubmit = {
                    val result = userService.disableUsers(model.`object`.map { it.email })
                    usersModel.`object` = result.map { UserRow(
                        it.mail,
                        it.disabled,
                        formatter.format(it.lockedUntil),
                        formatter.format(it.accountValidUntil),
                        formatter.format(it.credentialsValidUntil),
                    ) }
                }
            })
        }
        +DmContainer("inviteSection") {
            isVisible = inviteMode
            add(DmFeedbackPanel("feedback"))
            add(DmForm("form", invitationFormModel) {
                onSubmit = {
                    val redirectUri = wicketContextProperties.baseUri.resolve(
                        requestCycle.mapUrlFor(
                            AcceptInvitationPage::class.java,
                            PageParameters()
                        ).toString()
                    )
                    userService.inviteUser(
                        invitationFormModel.`object`.emailAddress!!,
                        invitationFormModel.`object`.groupName!!,
                        redirectUri,
                    )
                    info("Invitation created for email='${'$'}{modelObject.emailAddress}' with group='${'$'}{modelObject.groupName}'")
                }
                +EmailTextField("emailAddress").apply {
                    isRequired = true
                    @Suppress("UNCHECKED_CAST")
                    run {
                        add(EmailAddressValidator.getInstance() as IValidator<String>)
                        add(StringValidator.maximumLength(320) as IValidator<String>)
                    }
                }
                +DmDropDownChoice(
                    "groupName",
                    PropertyModel(invitationFormModel, "groupName"),
                    groupChoicesModel
                ) {
                    isRequired = true
                }
            })
        }
    }
}
