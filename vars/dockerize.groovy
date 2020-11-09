import org.lawrencedang.SlackSender

def call(String repoName, String versionTag, boolean useTrivy = false, String registryURL = '', String credentialsId = 'docker_hub_credentials', context)
{
    stage('Docker build')
    {
        docker.image('docker').inside('-v /var/run/docker.sock:/var/run/docker.sock')
        {
            SlackSender.onComplete("${context.env.JOB_NAME}'s Docker image built successfully",
            "${context.env.JOB_NAME}'s Docker image failed to build")
            {
                image = docker.build(repoName)
            }
            sh "docker tag ${repoName} ${repoName}:${versionTag}"
        }
    }

    if(useTrivy)
    {
        stage('Trivy scan')
        {
            SlackSender.onComplete("${context.env.JOB_NAME}'s trivy scan completed", 
            "${context.env.JOB_NAME}'s trivy scan failed") {
                docker.image('aquasec/trivy').inside(['--net=host', 
                '-v /var/run/docker.sock:/var/run/docker.sock', '--entrypoint='])
                {
                    sh 'trivy image ' + "${repoName}:${versionTag}"
                }
            }
        }
    }

    stage('Docker push')
    {
        docker.image('docker').inside('-v /var/run/docker.sock:/var/run/docker.sock')
        {
            SlackSender.onComplete("${context.env.JOB_NAME}'s Docker image pushed to registry",
            "${context.env.JOB_NAME}'s Docker image failed to push")
            {
                image = docker.image(repoName)
                docker.withRegistry(registryURL, credentialsId)
                {
                    image.push(versionTag)
                    image.push("latest")
                }
            }
        }
    }
    
}