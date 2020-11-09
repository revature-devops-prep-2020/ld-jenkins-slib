import org.lawrencedang.SlackSender

def call(String kubeMasterURL, context,  String kubeCredentialsId = 'kube-sa', String directory = './')
{
    SlackSender messenger = new SlackSender(this)

    stage('Deploy')
    {
        docker.image('reblank/kubectl_agent').inside('--net=host'){
            messenger.onComplete("${context.env.JOB_NAME} deployed to cluster.",
            "${context.env.JOB_NAME} failed to deploy to cluster.") {
                withKubeConfig([credentialsId: kubeCredentialsId, serverUrl: kubeMasterURL])
                {
                    sh "kubectl apply -f ${directory}"
                }
            }
        }
    }
}