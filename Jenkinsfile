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
        stage("docker image build"){
		    steps {
		        script{
		          bat "docker build -t sanniboena/dockerrepo:groceryapp ."
		        }
		    }
	    }
	    stage('Push image') {
	        steps{
	            script{
                     withCredentials([usernameColonPassword(credentialsId: 'Docker_Hub', variable: '')]) {
                  
                     bat "docker push sanniboena/dockerrepo:groceryapp"
                    }
	                
	            }
	        }
        } 
	   stage('Deploying Kubernetes') {
	      steps {
		script {
		  kubernetesDeploy(configs: "deployment.yaml", "service.yaml")
		}
	      }
   	 }
    }
}
