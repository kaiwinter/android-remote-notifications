package com.github.kaiwinter.androidremotenotifications.model.impl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.github.kaiwinter.androidremotenotifications.model.buttonaction.ButtonAction;

/**
 * Shows a Notification in an {@link AlertDialog}.
 */
public final class AlertDialogNotification extends AbstractUserNotification {

    /**
     * Title of the notification.
     */
    private String title;

    /**
     * The message in the notification.
     */
    private String message;

    /**
     * The caption of the negative button. If <code>null</code> the button won't be shown.
     */
    private String negativeButtonText;

    /**
     * The caption of the neutral button. If <code>null</code> the button won't be shown.
     */
    private String neutralButtonText;

    /**
     * The caption of the positive button. If <code>null</code> the button won't be shown.
     */
    private String positiveButtonText;

    /**
     * Action to carry out when the user selects the positive button. If <code>null</code> the dialog just gets closed.
     */
    private ButtonAction positiveButtonAction;

    /**
     * Action to carry out when the user selects the negative button. If <code>null</code> the dialog just gets closed.
     */
    private ButtonAction negativeButtonAction;

    /**
     * Action to carry out when the user selects the neutral button. If <code>null</code> the dialog just gets closed.
     */
    private ButtonAction neutralButtonAction;

    /**
     * If true, the user must push the positive or negative button. The back button and tapping the app in the
     * background won't close the dialog.
     */
    private boolean modal;

    public AlertDialogNotification() {
        super();
    }

    public AlertDialogNotification(NotificationConfiguration notificationConfiguration) {
        super(notificationConfiguration);
    }

    @Override
    public void show(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        if (negativeButtonText != null) {
            DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (negativeButtonAction != null) {
                        negativeButtonAction.execute(context);
                    }
                    dialog.dismiss();
                }
            };
            builder.setNegativeButton(negativeButtonText, negativeListener);
        }

        if (neutralButtonText != null) {
            DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (neutralButtonAction != null) {
                        neutralButtonAction.execute(context);
                    }
                    dialog.dismiss();
                }
            };
            builder.setNeutralButton(neutralButtonText, neutralListener);
        }

        if (positiveButtonText != null) {
            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (positiveButtonAction != null) {
                        positiveButtonAction.execute(context);
                    }
                    dialog.dismiss();
                }
            };
            builder.setPositiveButton(positiveButtonText, positiveListener);
        }

        if (modal) {
            builder.setCancelable(false);
        }

        builder.create().show();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public ButtonAction getPositiveButtonAction() {
        return positiveButtonAction;
    }

    public void setPositiveButtonAction(ButtonAction positiveButtonAction) {
        this.positiveButtonAction = positiveButtonAction;
    }

    public ButtonAction getNegativeButtonAction() {
        return negativeButtonAction;
    }

    public void setNegativeButtonAction(ButtonAction negativeButtonAction) {
        this.negativeButtonAction = negativeButtonAction;
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public String getNeutralButtonText() {
        return neutralButtonText;
    }

    public void setNeutralButtonText(String neutralButtonText) {
        this.neutralButtonText = neutralButtonText;
    }

    public ButtonAction getNeutralButtonAction() {
        return neutralButtonAction;
    }

    public void setNeutralButtonAction(ButtonAction neutralButtonAction) {
        this.neutralButtonAction = neutralButtonAction;
    }

    @Override
    public String toString() {
        return "AlertDialogNotification{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AlertDialogNotification that = (AlertDialogNotification) o;

        if (modal != that.modal) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (negativeButtonText != null ? !negativeButtonText.equals(that.negativeButtonText) : that.negativeButtonText != null)
            return false;
        if (neutralButtonText != null ? !neutralButtonText.equals(that.neutralButtonText) : that.neutralButtonText != null)
            return false;
        if (positiveButtonText != null ? !positiveButtonText.equals(that.positiveButtonText) : that.positiveButtonText != null)
            return false;
        if (positiveButtonAction != null ? !positiveButtonAction.equals(that.positiveButtonAction) : that.positiveButtonAction != null)
            return false;
        if (negativeButtonAction != null ? !negativeButtonAction.equals(that.negativeButtonAction) : that.negativeButtonAction != null)
            return false;
        return !(neutralButtonAction != null ? !neutralButtonAction.equals(that.neutralButtonAction) : that.neutralButtonAction != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (negativeButtonText != null ? negativeButtonText.hashCode() : 0);
        result = 31 * result + (neutralButtonText != null ? neutralButtonText.hashCode() : 0);
        result = 31 * result + (positiveButtonText != null ? positiveButtonText.hashCode() : 0);
        result = 31 * result + (positiveButtonAction != null ? positiveButtonAction.hashCode() : 0);
        result = 31 * result + (negativeButtonAction != null ? negativeButtonAction.hashCode() : 0);
        result = 31 * result + (neutralButtonAction != null ? neutralButtonAction.hashCode() : 0);
        result = 31 * result + (modal ? 1 : 0);
        return result;
    }
}
