package org.lawrencedang;

class SlackSender {

    static final String SUCCESS_COLOR = '#00FF00'
    static final String FAIL_COLOR = '#FF0000'

    def context;

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
            this.context.slackSend(color: SUCCESS_COLOR, message: failure)
            this.context.error(failure)
            return
        }
        this.context.slackSend(color: FAIL_COLOR, message: success)
    }
}