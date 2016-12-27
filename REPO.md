# Using this repo

Current version is defined in *version.txt*

**Installing a local SNAPSHOT**

```shell-script
> ./gradlew clean build publishToMavenLocal
```

**Release a new remote SNAPSHOT**

```shell-script
> ./gradlew clean gitRelease -Psnapshot=true
```

**Release a new version to Bintray**

1) Run the publishRelease task

```shell-script
> ./gradlew clean gitRelease
```

2) Goto https://bintray.com/buchandersenn/maven/android-realm-builders and release the new artifacts