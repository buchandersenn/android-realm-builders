# Using this repo

Current version is defined in *version.txt*

**Installing a local SNAPSHOT**

```shell-script
> ./gradlew build publishToMavenLocal
```

**Release a new remote SNAPSHOT**

```shell-script
> ./gradlew clean artifactoryPublish
```

**Release a new version to Bintray**

1) Check if there are uncommitted changes in the repo; nly proceed if the repo is clean

```shell-script
> if [[ -n $(git status --porcelain) ]]; then echo "repo is dirty"; fi
```

2) Increment version number, build new artifacts and upload them to bintray then commit changes

```shell-script
> ./gradlew release
> ./gradlew clean bintrayUpload
> git push --follow-tags
```

3) Goto https://bintray.com/buchandersenn/maven/android-realm-builders and release artifacts.