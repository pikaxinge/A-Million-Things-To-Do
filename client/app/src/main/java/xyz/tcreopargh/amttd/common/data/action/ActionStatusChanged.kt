package xyz.tcreopargh.amttd.common.data.action

import android.text.Spannable
import android.text.SpannableString
import xyz.tcreopargh.amttd.R
import xyz.tcreopargh.amttd.common.data.TodoStatus
import xyz.tcreopargh.amttd.common.data.UserImpl
import xyz.tcreopargh.amttd.util.getColor
import xyz.tcreopargh.amttd.util.i18n
import xyz.tcreopargh.amttd.util.plus
import xyz.tcreopargh.amttd.util.setColor
import java.util.*

/**
 * @author TCreopargh
 */
class ActionStatusChanged(
    override val actionId: UUID,
    override val user: UserImpl?,
    override val timeCreated: Calendar,
    override val fromStatus: TodoStatus,
    override val toStatus: TodoStatus
) : IAction {
    override fun getActionText(): Spannable {
        return SpannableString(user?.username + " ")
            .setColor(getColor(R.color.primary)) +
                SpannableString(i18n(R.string.change_status)) + SpannableString(" ") +
                fromStatus.getColoredString() + SpannableString(" ") +
                SpannableString(i18n(R.string.change_status_to)) + SpannableString(" ") +
                toStatus.getColoredString()
    }

    override val actionType: ActionType = ActionType.STATUS_CHANGED

    override fun getImageRes(): Int = R.drawable.ic_baseline_done_all_24
}