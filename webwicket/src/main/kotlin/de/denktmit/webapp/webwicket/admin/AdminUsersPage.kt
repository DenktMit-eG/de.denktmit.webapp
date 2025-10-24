package de.denktmit.webapp.webwicket.admin

import de.denktmit.webapp.webwicket.layout.SidebarBasePage
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

class AdminUsersPage(
    pageParameters: PageParameters?,
) : SidebarBasePage(pageParameters, AdminSidebarPanel()) {

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
            // TODO: Replace with real data source (service/DAO)
            return listOf(
                UserRow(
                    email = "user1.johndoe@example.com",
                    disabled = false,
                    lockedUntil = "Wed, 1 Jan 1000 00:00:00 GMT",
                    accountValidUntil = "Wed, 1 Jan 3000 00:00:00 GMT",
                    credentialsValidUntil = "Wed, 1 Jan 3000 00:00:00 GMT",
                ),
                UserRow(
                    email = "admin2.janesmith@example.com",
                    disabled = false,
                    lockedUntil = "Wed, 1 Jan 1000 00:00:00 GMT",
                    accountValidUntil = "Wed, 1 Jan 3000 00:00:00 GMT",
                    credentialsValidUntil = "Wed, 1 Jan 3000 00:00:00 GMT",
                ),
                UserRow(
                    email = "creds_expired_user5.petergabriel@example.com",
                    disabled = true,
                    lockedUntil = "Wed, 1 Jan 1000 00:00:00 GMT",
                    accountValidUntil = "Wed, 1 Jan 3000 00:00:00 GMT",
                    credentialsValidUntil = "Wed, 1 Jan 1000 00:00:00 GMT",
                ),
            )
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
                // TODO: Wire to service sending the invitation
                // For now, we just report success
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
