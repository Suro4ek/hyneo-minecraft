#!/usr/bin/env groovy

 pipeline {

     agent any

     tools {
         jdk "JDK17"
     }
     environment {
        NEXUS = credentials('nexus')
     }

     stages {
         stage('Build') {
             steps {
                 sh 'echo Build project'
                 sh 'chmod +x gradlew'
                 sh './gradlew api:clean api:shadowJar api:publish --stacktrace --warn'
                 archiveArtifacts artifacts: 'api/build/libs/*.jar'
             }
         }
     }
}