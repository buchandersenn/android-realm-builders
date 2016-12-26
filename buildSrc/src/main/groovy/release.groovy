package com.github.buchandersenn

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GitRelease implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("gitRelease", GitReleaseExtension)

        project.task('gitRelease', dependsOn: 'prepareNextGitSnapshot') {
            doLast {
                println 'release done!'
                println 'Goto https://bintray.com/buchandersenn/maven/android-realm-builders and release the published artifacts'
            }
        }

        project.task('prepareNextGitSnapshot', dependsOn: 'tagGitRelease') {
            doLast {
                // Get current version
                def versionFile = new File("${project.rootDir}/version.txt")
                def currentVersion = versionFile.text.trim()

                // Calculate next version
                def newVersion;
                if (project.hasProperty("nextVersion")) {
                    newVersion = nextVersion
                } else {
                    // TODO : Increase either major, minor or patch
                    currentVersion = currentVersion.replace('-SNAPSHOT', '')
                    def split = currentVersion.split('\\.', 3)
                    def newMajorVersion = split[0].toInteger()
                    def newMinorVersion = split[1].toInteger() + 1
                    def newPatchVersion = 0
                    newVersion = newMajorVersion + '.' + newMinorVersion + '.' + newPatchVersion + '-SNAPSHOT'
                }

                // Set next snapshot version
                versionFile.write(newVersion)

                // Commit new version
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'add', 'version.txt'
//                }
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'commit', '-m', "prepare next version ${newVersion}"
//                }
//
//                // Push all, including tag
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'push', '--follow-tags'
//                }
            }
        }

        project.task('tagGitRelease', dependsOn: 'performReleaseTasks') {
            doLast {
                // Get version
                def versionFile = new File("${project.rootDir}/version.txt")
                def currentVersion = versionFile.text.trim()

                // Tag release
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'tag', '-a', 'v' + currentVersion, '-m', 'version ' + currentVersion
//                }
            }
        }

        project.task('performReleaseTasks', dependsOn: 'prepareGitRelease') {
        }

        project.task('prepareGitRelease', dependsOn: 'checkGitRepo') {
            doLast {
                // Set release version by removing -SNAPSHOT
                def versionFile = new File("${rootDir}/version.txt")
                def currentVersion = versionFile.text.trim()
                def newVersion = currentVersion.replace('-SNAPSHOT', '')
                versionFile.write(newVersion)

                // Commit new version
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'add', 'version.txt'
//                }
//                exec {
//                    workingDir rootDir
//                    commandLine 'git'
//                    args 'commit', '-m', "prepare release ${newVersion}"
//                }
            }
            onlyIf { !(project.hasProperty('snapshot') && project.snapshot.toBoolean())}
        }

        project.task('checkGitRepo') {
            outputs.upToDateWhen { false }
            doLast {
                def status
                new ByteArrayOutputStream().withStream { os ->
                    project.exec {
                        workingDir project.rootDir
                        commandLine 'git'
                        args 'status', '--porcelain'
                        standardOutput = os
                    }
                    status = os.toString()
                }

                if (status != null && !status.equals("")) {
                    throw new GradleException("repo is dirty - cannot publish releases from a repository containing uncommited changes")
                }
            }
        }

        project.afterEvaluate {
            project.tasks['performReleaseTasks'].dependsOn project.gitRelease.releaseTasks
            for (Task releaseTask : project.gitRelease.releaseTasks) {
                releaseTask.mustRunAfter project.tasks['prepareGitRelease']
            }
        }
    }
}

class GitReleaseExtension {
    def Task[] releaseTasks = []
}
