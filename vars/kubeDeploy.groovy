import org.lawrencedang.SlackSender

def call(String kubeMasterURL, context,  String credentialsId = 'kube-sa', String directory = './')
{
    SlackSender messenger = new SlackSender(this)

    stage('Deploy')
    {
        docker.image('reblank/kubectl_agent').inside('--net=host'){
            messenger.onComplete("${context.env.JOB_NAME} deployed to cluster.",
            "${context.env.JOB_NAME} failed to deploy to cluster.") {
                sh "kubectl apply -f ${directory}"
            }
        }
    }
}