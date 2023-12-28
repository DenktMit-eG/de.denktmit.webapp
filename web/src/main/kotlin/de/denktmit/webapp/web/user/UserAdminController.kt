package de.denktmit.webapp.web.user

import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.persistence.rbac.GROUP_NAME_ADMINS
import de.denktmit.webapp.persistence.rbac.GROUP_NAME_USERS
import de.denktmit.webapp.springconfig.WebContextProperties
import de.denktmit.webapp.web.user.internal.InvitationFormData
import de.denktmit.webapp.web.user.internal.UniqueEmailMessageCode
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/users")
class UserAdminController(
    private val properties: WebContextProperties,
    private val userService: UserService,
) {

    @GetMapping
    fun getUsers(
        model: Model
    ): String {
        val dataTable = userService.userData()
        model.addAttribute("dataTable", dataTable)
        return "admin/users"
    }

    @GetMapping(params = arrayOf("action=invite"))
    fun inviteUsersForm(
        model: Model
    ): String {
        model.addAttribute("invitationForm", InvitationFormData())
        model.addAttribute("userGroupChoices", arrayOf(GROUP_NAME_ADMINS, GROUP_NAME_USERS))
        return "admin/inviteform"
    }

    @PostMapping(params = arrayOf("action=invite"))
    fun inviteUsers(
        @Validated @ModelAttribute("invitationForm") formModel: InvitationFormData,
        bindingResult: BindingResult,
        model: Model
    ): String {
        model.addAttribute("userGroupChoices", arrayOf(GROUP_NAME_ADMINS, GROUP_NAME_USERS))
        if (bindingResult.hasErrors() || inviteUser(formModel, bindingResult) !is UserSavingResult.Persisted) {
            return "admin/inviteform"
        }
        return "redirect:/admin/users"
    }

    private fun inviteUser(formModel: InvitationFormData, bindingResult: BindingResult): UserSavingResult {
        val invitationAcceptUri = properties.baseUri.resolve("/invite-accept")
        val result = userService.inviteUser(formModel.emailAddress, formModel.groupName, invitationAcceptUri)
        if (result is UserSavingResult.EmailAlreadyExists) {
            bindingResult.rejectValue(formModel::emailAddress.name, UniqueEmailMessageCode)
        }
        return result
    }


}