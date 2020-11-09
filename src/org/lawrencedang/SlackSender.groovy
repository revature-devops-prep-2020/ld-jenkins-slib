package org.lawrencedang;

class SlackSender {

    static final String SUCCESS_COLOR = '#00FF00'
    static final String FAIL_COLOR = '#FF0000'

    public SlackSender(context)
    {
        this.context = context
    }

    void onComplete(String success, String failure, Closure script)
    {
        try
        {
            script()
        }
        catch(Exception e)
        {
            context.slackSend(color: SUCCESS_COLOR, message: failure)
            error(failure)
            return
        }
        context.slackSend(color: FAIL_COLOR, message: success)
    }
}