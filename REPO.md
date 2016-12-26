# Using this repo

Current version is defined in *version.txt*

**Installing a local SNAPSHOT**

```shell-script
> ./gradlew clean build publishToMavenLocal
```

**Release a new remote SNAPSHOT**

```shell-script
> ./gradlew clean artifactoryPublish
```

**Release a new version to Bintray**

1) Check if there are uncommitted changes in the repo; Only proceed if the repo is clean

2) Prepare release by removing -SNAPSHOT from version.txt and commit change

3) Build release and upload to bintray

```shell-script
> ./gradlew clean bintrayUpload
```

4) Create git tag and push to origin

```shell-script
> git tag -a vX.Y.Z -m "version X.Y.Z"
> git push --follow-tags
```

5) Prepare for next release by increasing version number in version.txt and commit change

3) Goto https://bintray.com/buchandersenn/maven/android-realm-builders and release artifacts.