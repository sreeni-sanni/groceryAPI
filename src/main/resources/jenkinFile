pipeline {
    agent any

    tools {
       
        maven "maven-3"
    }

    stages {
        stage('Checkout') {
            steps {
               checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/sreeni-sanni/groceryAPI.git']])
            }

            
        }
        stage("Maven Build"){
            steps{
                bat 'mvn clean install'
            }
        }
    }
}
