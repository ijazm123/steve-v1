pipeline{
    agent any
        stages{
            stage(" Git clone"){
                steps{
                    git 'https://github.com/ijazm123/steve-v1.git'
                }
        }
        stage("Kill current jar"){
            steps{
                sh "sudo pkill -f 'java -jar target/steve.jar'"
            }
        }
        stage("package"){
            steps{
                sh "mvn clean package"
            }
        }
        stage("run jar"){
            steps{
                sh "sudo nohup java -jar target/steve.jar &"
            }
        }
    }
}
