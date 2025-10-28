package de.denktmit.webapp.webwicket.admin

import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.springconfig.WicketContextProperties
import de.denktmit.webapp.webwicket.layout.SidebarBasePage
import de.denktmit.webapp.webwicket.user.AcceptInvitationPage
import de.denktmit.wicket.spring.bean
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.FeedbackPanel
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
    private val inviteMode: Boolean = pageParameters?.get("action")?.toString()?.equals("invite", ignoreCase = true) ?: false

    // Data model for the users table
    data class UserRow(
        val email: String,
        val disabled: Boolean,
        val lockedUntil: String,
        val accountValidUntil: String,
        val credentialsValidUntil: String,
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

        // Sections that are toggled
        val listSection = WebMarkupContainer("listSection").apply { isVisible = !inviteMode }
        val inviteSection = WebMarkupContainer("inviteSection").apply { isVisible = inviteMode }
        add(listSection)
        add(inviteSection)

        // Users list
        val listView = object : ListView<UserRow>("rows", usersModel) {
            override fun populateItem(item: ListItem<UserRow>) {
                val row = item.modelObject
                item.add(Label("email", row.email))
                item.add(Label("disabled", row.disabled.toString()))
                item.add(Label("lockedUntil", row.lockedUntil))
                item.add(Label("accountValidUntil", row.accountValidUntil))
                item.add(Label("credentialsValidUntil", row.credentialsValidUntil))
            }
        }
        listSection.add(listView)

        // Invite form
        val feedback = FeedbackPanel("feedback")
        inviteSection.add(feedback)

        val form = object : Form<InvitationForm>("form", invitationFormModel) {
            override fun onSubmit() {
                val redirectUri = wicketContextProperties.baseUri.resolve(requestCycle.mapUrlFor(AcceptInvitationPage::class.java, PageParameters()).toString())
                userService.inviteUser(
                    invitationFormModel.`object`.emailAddress!!,
                    invitationFormModel.`object`.groupName!!,
                    redirectUri,
                )
                info("Invitation created for email='${'$'}{modelObject.emailAddress}' with group='${'$'}{modelObject.groupName}'")
            }
        }
        inviteSection.add(form)

        val emailField = EmailTextField("emailAddress").apply {
            isRequired = true
            @Suppress("UNCHECKED_CAST")
            run {
                add(EmailAddressValidator.getInstance() as IValidator<String>)
                add(StringValidator.maximumLength(320) as IValidator<String>)
            }
        }
        form.add(emailField)

        val groupChoice = DropDownChoice<String>(
            "groupName",
            PropertyModel(invitationFormModel, "groupName"),
            groupChoicesModel
        )
        groupChoice.isRequired = true
        form.add(groupChoice)
    }
}
