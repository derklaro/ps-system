pipeline {
    agent any

    tools {
        jdk "1.8.0_222"
    }

    options {
        buildDiscarder logRotator(numToKeepStr: '10')
    }

    stages {
        stage('Clean') {
            steps {
                sh 'mvn clean';
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package';
            }
        }

        stage('Verify') {
            steps {
                sh 'mvn verify';
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'ps-plugin-spigot/target/ps-plugin-spigot.jar'
                archiveArtifacts artifacts: 'ps-api/target/ps-api.jar'
                archiveArtifacts artifacts: 'ps-cloudnet2/target/ps-cloudnet2.jar'
                archiveArtifacts artifacts: 'ps-cloudnet3/target/ps-cloudnet3.jar'
                archiveArtifacts artifacts: 'ps-reformcloud2/target/ps-reformcloud2.jar'
            }
        }
    }
}