# Using this repo

Current version is defined in *version.txt*

**Installing a local SNAPSHOT**

```shell-script
> ./gradlew clean build publishToMavenLocal
```

**Release a new version to Bintray**

```shell-script
> ./gradlew prepareRelease
> ./gradlew clean bintrayUpload
> ./gradlew finishRelease
```