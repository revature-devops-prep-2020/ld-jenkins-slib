package org.lawrencedang;

class SlackSender {

    static final String SUCCESS_COLOR = '#00FF00'
    static final String FAIL_COLOR = '#FF0000'

    static void onComplete(String success, String failure, Closure script)
    {
        try
        {
            script()
        }
        catch(Exception e)
        {
            slackSend(color: SUCCESS_COLOR, message: failure)
            error(failure)
            return
        }
        slackSend(color: FAIL_COLOR, message: success)
    }
}