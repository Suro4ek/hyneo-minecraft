#!/usr/bin/env groovy

 pipeline {

     agent any

     tools {
         jdk "jdk17"
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